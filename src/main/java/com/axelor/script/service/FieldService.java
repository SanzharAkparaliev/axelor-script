package com.axelor.script.service;

import com.axelor.meta.db.MetaModel;
import java.util.Map;

import java.util.List;

public interface FieldService {
    void generateMetaPermissionRules(List<MetaModel> models);
}