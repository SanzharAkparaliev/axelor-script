package com.axelor.script.service.impl;

import com.axelor.auth.db.Permission;
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
import com.google.inject.persist.Transactional;

import javax.security.auth.AuthPermission;
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
    public FieldServiceImpl(MetaModelRepository metaModelRepository,
                            MetaPermissionRuleRepository metaPermissionRuleRepository,
                            MetaPermissionRepository metaPermissionRepository, PermissionRepository permissionRepository) {
        this.metaModelRepository = metaModelRepository;
        this.metaPermissionRuleRepository = metaPermissionRuleRepository;
        this.metaPermissionRepository = metaPermissionRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    public void generateMetaPermissionRules() {
        List<MetaPermission> metaPermissions = metaPermissionRepository.all().fetch();
        List<Permission> permissions = permissionRepository.all().fetch();
        metaPermissions.forEach(this::processMetaPermission);
    }

    private void processMetaPermission(MetaPermission metaPermission) {
        MetaModel metaModel = findMetaModelByName(metaPermission.getObject());
        if (metaModel == null) {
            logger.warning("MetaModel not found for MetaPermission object: " + metaPermission.getObject());
            return;
        }
        List<MetaField> metaFields = scriptFindByModel(metaModel);
        updateMetaPermissionRules(metaPermission, metaFields);
    }

    private MetaModel findMetaModelByName(String name) {
        return metaModelRepository.all()
                .filter("self.fullName = :name")
                .bind("name", name)
                .fetchOne();
    }

    private void updateMetaPermissionRules(MetaPermission metaPermission, List<MetaField> metaFields) {
        Set<MetaPermissionRule> existingRules = new HashSet<>(metaPermission.getRules());

        for (MetaField metaField : metaFields) {
            if (!metaPermissionRuleExists(metaPermission, metaField)) {
                MetaPermissionRule newRule = buildMetaPermissionRule(metaField, metaPermission);
                MetaPermissionRule savedRule = metaPermissionRuleRepository.save(newRule);
                logger.info("MetaPermissionRule saved: " + savedRule.getId());
                existingRules.add(savedRule);
            }
        }

        metaPermission.getRules().clear();
        metaPermission.getRules().addAll(existingRules);

        MetaPermission savedMetaPermission = metaPermissionRepository.save(metaPermission);
        logger.info("MetaPermission saved: " + savedMetaPermission.getId());
    }

    private MetaPermissionRule buildMetaPermissionRule(MetaField metaField, MetaPermission metaPermission) {
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
        return Query.of(MetaField.class)
                .filter("self.relationship IS NULL AND self.metaModel = :metaModel")
                .bind("metaModel", metaModel)
                .fetch();
    }
}
