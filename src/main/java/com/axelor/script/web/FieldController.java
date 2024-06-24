package com.axelor.script.web;

import com.axelor.meta.db.MetaModel;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.script.service.FieldService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

import java.util.List;

@Singleton
public class FieldController {

  private final FieldService fieldService;

  @Inject
  public FieldController(FieldService fieldService) {
    this.fieldService = fieldService;
  }

  @Transactional
  public void generateField(ActionRequest request, ActionResponse response) {
    List<MetaModel> models = (List<MetaModel>) request.getContext().get("model");
    fieldService.generateMetaPermissionRules(models);
  }
}
