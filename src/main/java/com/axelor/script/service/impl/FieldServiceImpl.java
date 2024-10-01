package com.axelor.script.service.impl;

import com.axelor.auth.db.Role;
import com.axelor.auth.db.repo.RoleRepository;
import com.axelor.meta.db.MetaField;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.MetaPermission;
import com.axelor.meta.db.MetaPermissionRule;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.meta.db.repo.MetaPermissionRepository;
import com.axelor.meta.db.repo.MetaPermissionRuleRepository;
import com.axelor.script.service.FieldService;
import com.google.inject.Inject;
import java.util.*;
import java.util.logging.Logger;

public class FieldServiceImpl implements FieldService {
  private static final Logger logger = Logger.getLogger(FieldServiceImpl.class.getName());

  private final MetaModelRepository metaModelRepository;
  private final MetaPermissionRuleRepository metaPermissionRuleRepository;
  private final MetaPermissionRepository metaPermissionRepository;
  private final RoleRepository roleRepository;

  @Inject
  public FieldServiceImpl(
      MetaModelRepository metaModelRepository,
      MetaPermissionRuleRepository metaPermissionRuleRepository,
      MetaPermissionRepository metaPermissionRepository,
      RoleRepository roleRepository) {
    this.metaModelRepository = metaModelRepository;
    this.metaPermissionRuleRepository = metaPermissionRuleRepository;
    this.metaPermissionRepository = metaPermissionRepository;
    this.roleRepository = roleRepository;
  }

  @Override
  public void generateMetaPermissionRules(List<MetaModel> models) {
    if (models == null) {
      logger.severe("models list is null");
      return;
    }
    logger.info("Started generating meta permissions.");

    roleRepository
        .all()
        .fetch()
        .forEach(
            role -> {
              Set<MetaPermission> metaPermissionsSet = new HashSet<>();

              for (MetaModel metaModel : models) {
                List<MetaField> metaFields = scriptFindByModel(metaModel);
                if (metaFields == null || metaFields.isEmpty()) {
                  continue;
                }

                boolean hasGeneratableFields = metaFields.stream().anyMatch(MetaField::getGenerate);

                if (hasGeneratableFields) {
                  MetaPermission metaPermission = generatePermission(role, metaModel);
                  metaPermissionsSet.add(metaPermission);

                  metaFields.stream()
                      .filter(MetaField::getGenerate)
                      .forEach(
                          metaField -> {
                            logger.info("Processing field: " + metaField.getName());
                            processMetaPermission(metaPermission, metaField);
                          });
                } else {
                  logger.info(
                      "No generatable fields found for MetaModel: "
                          + metaModel.getFullName()
                          + ", skipping MetaPermission creation.");
                }
              }

              role.setMetaPermissions(metaPermissionsSet);
              roleRepository.save(role);
            });

    logger.info("Finished generating meta permissions.");
  }

  private MetaPermission generatePermission(Role role, MetaModel metaModel) {
    String permissionName = String.format("%s.%s", role.getName(), metaModel.getName());
    MetaPermission metaPermission = findOrCreateMetaPermission(metaModel, permissionName);

    initializeMetaPermissionRules(metaPermission, metaModel);

    return metaPermission;
  }

  private MetaPermission findOrCreateMetaPermission(MetaModel metaModel, String permissionName) {
    MetaPermission metaPermission =
        metaPermissionRepository
            .all()
            .filter("self.name = :name")
            .bind("name", permissionName)
            .fetchOne();
    MetaModel model =
        metaModelRepository.all().filter("self.id = :id").bind("id", metaModel.getId()).fetchOne();

    if (metaPermission == null) {
      metaPermission = new MetaPermission();
      metaPermission.setName(permissionName);
      metaPermission.setObject(model.getFullName());
      metaPermission = metaPermissionRepository.save(metaPermission);
      logger.info(
          "Created new MetaPermission: "
              + metaPermission.getName()
              + " for MetaModel: "
              + model.getFullName());
    }

    return metaPermission;
  }

  private void initializeMetaPermissionRules(MetaPermission metaPermission, MetaModel metaModel) {
    if (metaPermission.getRules() == null) {
      metaPermission.setRules(new ArrayList<>());
    }

    if (metaPermission.getRules().isEmpty()) {
      List<MetaField> metaFields = scriptFindByModel(metaModel);
      metaFields.stream()
          .filter(MetaField::getGenerate)
          .forEach(
              metaField -> {
                MetaPermissionRule rule = buildMetaPermissionRule(metaField, metaPermission);
                metaPermission.getRules().add(rule);
                metaPermissionRuleRepository.save(rule);
              });
      metaPermissionRepository.save(metaPermission);
    }
  }

  private void processMetaPermission(MetaPermission metaPermission, MetaField metaField) {
    if (metaPermission == null) {
      logger.warning("MetaPermission is null for MetaField: " + metaField.getName());
      return;
    }

    updateMetaPermissionRules(metaPermission, metaField);
  }

  private void updateMetaPermissionRules(MetaPermission metaPermission, MetaField metaField) {
    Set<MetaPermissionRule> existingRules =
        new HashSet<>(
            metaPermission.getRules() != null ? metaPermission.getRules() : Collections.emptySet());
    logger.info(
        "Existing rules for MetaPermission " + metaPermission.getName() + ": " + existingRules);

    if (!metaPermissionRuleExists(metaPermission, metaField)) {
      MetaPermissionRule newRule = buildMetaPermissionRule(metaField, metaPermission);
      MetaPermissionRule savedRule = metaPermissionRuleRepository.save(newRule);
      existingRules.add(savedRule);
      logger.info("MetaPermissionRule saved: " + savedRule.getId());
    }

    metaPermission.getRules().clear();
    metaPermission.getRules().addAll(existingRules);

    MetaPermission savedMetaPermission = metaPermissionRepository.save(metaPermission);
    logger.info("MetaPermission saved: " + savedMetaPermission.getName());
  }

  private MetaPermissionRule buildMetaPermissionRule(
      MetaField metaField, MetaPermission metaPermission) {
    MetaPermissionRule metaPermissionRule = new MetaPermissionRule();
    metaPermissionRule.setField(metaField.getName());
    metaPermissionRule.setCanRead(true);
    metaPermissionRule.setCanExport(true);
    metaPermissionRule.setCanWrite(true);
    metaPermissionRule.setMetaPermission(metaPermission);
    return metaPermissionRule;
  }

  private boolean metaPermissionRuleExists(MetaPermission metaPermission, MetaField metaField) {
    return metaPermission.getRules().stream()
        .anyMatch(rule -> rule.getField().equals(metaField.getName()));
  }

  private List<MetaField> scriptFindByModel(MetaModel metaModel) {
    if (metaModel == null) {
      logger.severe("metaModel cannot be null");
      throw new IllegalArgumentException("metaModel cannot be null");
    }
    return metaModel.getMetaFields();
  }
}
