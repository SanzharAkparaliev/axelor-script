package com.axelor.message.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.db.Query;
import com.axelor.message.db.TemplateContext;

public class TemplateContextRepository extends JpaRepository<TemplateContext> {

  public TemplateContextRepository() {
    super(TemplateContext.class);
  }

  public TemplateContext findByName(String name) {
    return Query.of(TemplateContext.class)
      .filter("self.name = :name")
      .bind("name", name)
      .fetchOne();
  }
}
