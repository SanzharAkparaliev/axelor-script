package com.axelor.studio.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.studio.db.AppScript;

public class AppScriptRepository extends JpaRepository<AppScript> {

  public AppScriptRepository() {
    super(AppScript.class);
  }
}
