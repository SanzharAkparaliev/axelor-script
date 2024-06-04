package com.axelor.script.web;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.script.service.PermissionService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

@Singleton
public class PermissionController {
    private final PermissionService permissionService;

    @Inject
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Transactional
    public void generatePermission(ActionRequest request, ActionResponse response) {
        permissionService.generateMetaPermissions();
    }
}
