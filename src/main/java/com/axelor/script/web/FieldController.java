package com.axelor.script.web;

import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.script.service.FieldService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;


@Singleton
public class FieldController {

    private final FieldService fieldService;

    @Inject
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @Transactional
    public void generateField(ActionRequest request, ActionResponse response) {
        fieldService.generateMetaPermissionRules();
    }}
