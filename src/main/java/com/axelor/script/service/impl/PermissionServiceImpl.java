package com.axelor.script.service.impl;

import com.axelor.auth.db.Permission;
import com.axelor.auth.db.Role;
import com.axelor.auth.db.repo.PermissionRepository;
import com.axelor.auth.db.repo.RoleRepository;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.script.service.PermissionService;
import com.google.inject.Inject;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.List;

import org.slf4j.Logger;

public class PermissionServiceImpl implements PermissionService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final MetaModelRepository modelRepository;
    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Inject
    public PermissionServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository, MetaModelRepository modelRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.modelRepository = modelRepository;
    }

    @Override
    public void generateMetaPermissions() {
        logger.info("Starting generating meta permissions...");

        List<Role> roles = roleRepository.all().fetch();
        List<MetaModel> models = modelRepository.all().fetch();

        roles.forEach(role -> models.forEach(model -> generatePermission(role, model)));

        logger.info("Finished generating meta permissions.");
    }

    private void generatePermission(Role role, MetaModel metaModel) {
        String permissionName = role.getName() + "." + metaModel.getName();
        if (permissionRepository.findByName(permissionName) == null) {
            Permission permission = new Permission();
            permission.setCanCreate(true);
            permission.setCanRead(true);
            permission.setCanExport(true);
            permission.setCanWrite(true);
            permission.setCanRemove(true);
            permission.setName(permissionName);
            permission.setObject(metaModel.getFullName());
            permissionRepository.save(permission);
            logger.info("Created permission: {}", permissionName);
        } else {
            logger.debug("Permission {} already exists, skipping creation.", permissionName);
        }
    }

}
