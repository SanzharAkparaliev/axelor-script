package com.axelor.script.service;

import com.axelor.meta.db.MetaModel;
import java.util.List;

public interface PermissionService {
  void generateMetaPermissions(List<MetaModel> model);

  void setModelToApp();

  void selectAll();
}
