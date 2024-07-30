package com.axelor.studio.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.db.Query;
import com.axelor.studio.db.App;

public class AppRepository extends JpaRepository<App> {

  public AppRepository() {
    super(App.class);
  }

  public App findByCode(String code) {
    return Query.of(App.class)
      .filter("self.code = :code")
      .bind("code", code)
      .cacheable()
      .fetchOne();
  }

  public App findByName(String name) {
    return Query.of(App.class)
      .filter("self.name = :name")
      .bind("name", name)
      .cacheable()
      .fetchOne();
  }

  // TYPE SELECT
  public static final String TYPE_STANDARD = "standard";
  public static final String TYPE_ADDONS = "addons";
  public static final String TYPE_ENTERPRISE = "enterprise";
  public static final String TYPE_CUSTOM = "custom";
  public static final String TYPE_OTHERS = "others";
}
