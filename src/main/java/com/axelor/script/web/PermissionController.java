package com.axelor.script.web;

import com.axelor.meta.db.MetaModel;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.script.service.PermissionService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import java.util.List;

@Singleton
public class PermissionController {
  private final PermissionService permissionService;

  @Inject
  public PermissionController(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  @Transactional
  public void generatePermission(ActionRequest request, ActionResponse response) {
    List<MetaModel> model = (List<MetaModel>) request.getContext().get("model");
    permissionService.generateMetaPermissions(model);
  }

  @Transactional
  public void setModel(ActionRequest request, ActionResponse response) {
    permissionService.setModelToApp();
    response.setReload(true);
  }

  @Transactional
  public void selectAll(ActionRequest request, ActionResponse response) {
    permissionService.selectAll();
    response.setReload(true);
  }
}
