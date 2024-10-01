package com.axelor.script.web;

import com.axelor.apps.base.service.exception.TraceBackService;
import com.axelor.inject.Beans;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.script.service.FieldService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

@Singleton
public class FieldController {

  private final FieldService fieldService;
  private final ObjectMapper objectMapper;

  @Inject
  public FieldController(FieldService fieldService, ObjectMapper objectMapper) {
    this.fieldService = fieldService;
    this.objectMapper = objectMapper;
  }

  @Transactional
  public void generateField(ActionRequest request, ActionResponse response) {

    try {
      fieldService.generateMetaPermissionRules(Beans.get(MetaModelRepository.class).all().fetch());
    } catch (Exception e) {
      TraceBackService.trace(response, e);
    }
  }
}
