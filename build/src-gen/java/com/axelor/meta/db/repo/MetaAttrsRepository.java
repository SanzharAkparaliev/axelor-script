package com.axelor.meta.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.db.Query;
import com.axelor.meta.db.MetaAttrs;

public class MetaAttrsRepository extends JpaRepository<MetaAttrs> {

  public MetaAttrsRepository() {
    super(MetaAttrs.class);
  }

  public MetaAttrs findByName(String name) {
    return Query.of(MetaAttrs.class)
      .filter("self.name = :name")
      .bind("name", name)
      .fetchOne();
  }
}
