package com.axelor.script.service.impl;

import com.axelor.auth.db.repo.PermissionRepository;
import com.axelor.db.Query;
import com.axelor.meta.db.MetaField;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.MetaPermission;
import com.axelor.meta.db.MetaPermissionRule;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.meta.db.repo.MetaPermissionRepository;
import com.axelor.meta.db.repo.MetaPermissionRuleRepository;
import com.axelor.script.service.FieldService;
import com.google.inject.Inject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class FieldServiceImpl implements FieldService {
    private static final Logger logger = Logger.getLogger(FieldServiceImpl.class.getName());

    private final MetaModelRepository metaModelRepository;
    private final MetaPermissionRuleRepository metaPermissionRuleRepository;
    private final MetaPermissionRepository metaPermissionRepository;
    private final PermissionRepository permissionRepository;

    @Inject
    public FieldServiceImpl(
            MetaModelRepository metaModelRepository,
            MetaPermissionRuleRepository metaPermissionRuleRepository,
            MetaPermissionRepository metaPermissionRepository,
            PermissionRepository permissionRepository) {
        this.metaModelRepository = metaModelRepository;
        this.metaPermissionRuleRepository = metaPermissionRuleRepository;
        this.metaPermissionRepository = metaPermissionRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public void generateMetaPermissionRules(List<MetaModel> models) {
        if (models == null) {
            logger.severe("models list is null");
            return;
        }

        models.forEach(metaModel -> {
            if (metaModel == null) {
                logger.warning("metaModel is null in models list");
                return;
            }

            List<MetaField> metaFields = scriptFindByModel(metaModel);
            if (metaFields == null || metaFields.isEmpty()) {
                logger.warning("No MetaFields found for MetaModel: " + metaModel.getFullName());
                return;
            }

            for (MetaField metaField : metaFields) {
                if (metaField == null) {
                    logger.warning("metaField is null in metaFields list for MetaModel: " + metaModel.getFullName());
                    continue;
                }

                if (metaField.getGenerate()) {
                    System.out.println(metaField.getName());
                    List<MetaPermission> metaPermissions = metaPermissionRepository.all().fetch();
                    if (metaPermissions == null || metaPermissions.isEmpty()) {
                        logger.warning("No MetaPermissions found");
                        continue;
                    }

                    metaPermissions.forEach(metaPermission -> {
                        if (metaPermission == null) {
                            logger.warning("metaPermission is null in metaPermissions list");
                            return;
                        }

                        processMetaPermission(metaPermission, metaField);
                    });
                }
            }
        });
    }

    private void processMetaPermission(MetaPermission metaPermission, MetaField metaField) {
        MetaModel metaModel = findMetaModelByName(metaPermission.getObject());
        if (metaModel == null) {
            logger.warning("MetaModel not found for MetaPermission object: " + metaPermission.getObject());
            return;
        }

        updateMetaPermissionRules(metaPermission, metaField);
    }

    private MetaModel findMetaModelByName(String name) {
        return metaModelRepository.all().filter("self.fullName = :name").bind("name", name).fetchOne();
    }

    private void updateMetaPermissionRules(MetaPermission metaPermission, MetaField metaField) {
        Set<MetaPermissionRule> existingRules = new HashSet<>(metaPermission.getRules());
        logger.info("existingRules: " + existingRules);
        int counter = 0;

        if (!metaPermissionRuleExists(metaPermission, metaField)) {
            MetaPermissionRule newRule = buildMetaPermissionRule(metaField, metaPermission);
            MetaPermissionRule savedRule = metaPermissionRuleRepository.save(newRule);
            counter += 1;
            logger.info("MetaPermissionRule saved: " + savedRule.getId());
            existingRules.add(savedRule);
        }
        logger.info("Saved metaPermissionRule amount: " + counter);

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
