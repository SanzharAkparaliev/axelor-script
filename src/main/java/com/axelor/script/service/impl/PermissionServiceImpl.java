package com.axelor.script.service.impl;

import com.axelor.auth.db.Permission;
import com.axelor.auth.db.Role;
import com.axelor.auth.db.repo.PermissionRepository;
import com.axelor.auth.db.repo.RoleRepository;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.script.service.PermissionService;
import com.axelor.studio.db.App;
import com.axelor.studio.db.AppScript;
import com.axelor.studio.db.repo.AppRepository;
import com.axelor.studio.db.repo.AppScriptRepository;
import com.google.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PermissionServiceImpl implements PermissionService {

  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;
  private final MetaModelRepository modelRepository;
  private final AppRepository appRepository;
  private final AppScriptRepository appScriptRepository;
  private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

  @Inject
  public PermissionServiceImpl(
      RoleRepository roleRepository,
      PermissionRepository permissionRepository,
      MetaModelRepository modelRepository,
      AppRepository appRepository,
      AppScriptRepository appScriptRepository) {
    this.roleRepository = roleRepository;
    this.permissionRepository = permissionRepository;
    this.modelRepository = modelRepository;
    this.appRepository = appRepository;
    this.appScriptRepository = appScriptRepository;
  }

  @Override
  public void generateMetaPermissions(List<MetaModel> models) {
    logger.info("Starting generating meta permissions...");

    if (models == null || models.isEmpty()) {
      logger.warn("No models provided for generating meta permissions.");
      return;
    }

    models.stream()
        .filter(MetaModel::getGenerate)
        .forEach(
            model -> roleRepository.all().fetch().forEach(role -> generatePermission(role, model)));

    logger.info("Finished generating meta permissions.");
  }

  @Override
  public void setModelToApp() {
    App app =
        Optional.ofNullable(appRepository.findByCode("script"))
            .orElseThrow(() -> new IllegalStateException("App with code 'script' not found"));

    AppScript appScript =
        Optional.ofNullable(app.getAppScript())
            .orElseThrow(() -> new IllegalStateException("AppScript for app 'script' is not set"));

    List<MetaModel> models =
        Optional.ofNullable(modelRepository.all().fetch())
            .orElseThrow(
                () -> new IllegalStateException("Model repository returned null models list"));

    appScript.setModel(models);
    appScriptRepository.save(appScript);
  }

  @Override
  public void selectAll() {
    if (modelRepository == null) {
      logger.error("modelRepository is null");
      throw new NullPointerException("modelRepository is null");
    }

    List<MetaModel> models = modelRepository.all().fetch();
    if (models == null) {
      logger.error("modelRepository.all().fetch() returned null");
      throw new NullPointerException("modelRepository.all().fetch() returned null");
    }

    for (MetaModel model : models) {
      if (model != null) {
        try {
          model.setGenerate(true);
          modelRepository.save(model);
          logger.info("Model saved: " + model);
        } catch (Exception e) {
          logger.error("Failed to save model: " + model + ", exception: " + e.getMessage());
        }
      } else {
        logger.error("Encountered null model in list");
      }
    }
  }

  private void generatePermission(Role role, MetaModel metaModel) {
    String permissionName = String.format("%s.%s", role.getName(), metaModel.getName());

    Permission existingPermission = permissionRepository.findByName(permissionName);
    if (existingPermission != null) {
      logger.debug("Permission {} already exists, skipping creation.", permissionName);
      return;
    }

    createAndAssignPermission(role, metaModel, permissionName);
  }

  private void createAndAssignPermission(Role role, MetaModel metaModel, String permissionName) {
    Permission permission = new Permission();
    permission.setCanCreate(true);
    permission.setCanRead(true);
    permission.setCanExport(true);
    permission.setCanWrite(true);
    permission.setCanRemove(true);
    permission.setName(permissionName);
    permission.setObject(metaModel.getFullName());

    Permission savedPermission = permissionRepository.save(permission);

    Set<Permission> permissions = new HashSet<>(role.getPermissions());
    permissions.add(savedPermission);
    role.setPermissions(permissions);
    roleRepository.save(role);

    logger.info("Created permission: {}", permissionName);
  }
}
