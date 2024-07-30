package com.axelor.script.web;

import com.axelor.meta.db.MetaModel;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.axelor.script.service.FieldService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import java.util.Set;
import java.util.Map;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    Set<Map.Entry<String, Object>> entrySet = request.getContext().entrySet();

    Map.Entry<String, Object>[] entries = entrySet.toArray(new Map.Entry[0]);

    if (entries.length > 4) {
      Map.Entry<String, Object> entry = entries[4];

      Object value = entry.getValue();
      if (value instanceof List<?>) {
        try {
          List<?> valueList = (List<?>) value;
          List<MetaModel> models = objectMapper.convertValue(valueList, objectMapper.getTypeFactory().constructCollectionType(List.class, MetaModel.class));

          fieldService.generateMetaPermissionRules(models);
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else {
      }
    } else {
    }
  }
}
