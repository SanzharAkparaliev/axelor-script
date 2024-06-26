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
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.util.List;

@Singleton
public class FieldController {

  private final FieldService fieldService;
  private List<MetaModel> model;

  @Inject
  public FieldController(FieldService fieldService) {
    this.fieldService = fieldService;
  }

  @Transactional
  public void generateField(ActionRequest request, ActionResponse response) {
//    List<MetaModel> models = (List<MetaModel>) request.getContext().get("model");
//    for (MetaModel model : models) {
//      for (int j = 0; j < model.getMetaFields().size(); j++) {
//        ((ActionRequest) request).getData().get("context")
//                .entrySet().toArray()[3].getValue().get(models.indexOf(model)) //model -> name
//                .entrySet().toArray()[3].getValue().get(j) //field -> name
//                .entrySet().toArray()[5].getValue(); //field -> generate
//      }
//    }

    // Получаем entrySet
    Set<Map.Entry<String, Object>> entrySet = request.getContext().entrySet();

// Преобразуем entrySet в массив Map.Entry
    Map.Entry<String, List<MetaModel>>[] entries = entrySet.toArray(new Map.Entry[0]);

// Получаем 5-й элемент (с индексом 4)
    Map.Entry<String, List<MetaModel>> entry = entries[4];
    System.out.println("Entry value: " + entry.getValue());
    //LinkedHashMap<String, MetaModel> entryVal = entry.getValue();

// Получаем значение из этого элемента (должен быть список List<MetaModel>)
    List<MetaModel> models = (List<MetaModel>) entry;


    fieldService.generateMetaPermissionRules(models);
  }
}
