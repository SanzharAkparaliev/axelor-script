package com.axelor.meta.db.repo;

import com.axelor.db.JpaRepository;
import com.axelor.db.Query;
import com.axelor.meta.db.MetaView;

public class MetaViewRepository extends JpaRepository<MetaView> {

  public MetaViewRepository() {
    super(MetaView.class);
  }

  public MetaView findByID(String xmlId) {
    return Query.of(MetaView.class)
      .filter("self.xmlId = :xmlId")
      .bind("xmlId", xmlId)
      .fetchOne();
  }

  public MetaView findByName(String name) {
    return Query.of(MetaView.class)
      .filter("self.name = :name AND COALESCE(self.extension, FALSE) = FALSE")
      .bind("name", name)
      .order("-priority")
      .cacheable()
      .fetchOne();
  }

  public MetaView findByNameAndComputed(String name, Boolean computed) {
    return Query.of(MetaView.class)
      .filter("self.name = :name AND COALESCE(self.extension, FALSE) = FALSE       AND COALESCE(self.computed, FALSE) = :computed")
      .bind("name", name)
      .bind("computed", computed)
      .order("-priority")
      .cacheable()
      .fetchOne();
  }

  public Query<MetaView> findByModule(String module) {
    return Query.of(MetaView.class)
      .filter("self.module = :module")
      .bind("module", module)
      .cacheable();
  }

  public Query<MetaView> findByDependentFeature(String feature) {
    return Query.of(MetaView.class)
      .filter("COALESCE(self.extension, FALSE) = FALSE       AND COALESCE(self.computed, FALSE) = FALSE       AND (self.dependentFeatures LIKE :feature       OR self.dependentFeatures LIKE CONCAT('%,', :feature, ',%')       OR self.dependentFeatures LIKE CONCAT(:feature, ',%')       OR self.dependentFeatures LIKE CONCAT('%,', :feature))")
      .bind("feature", feature)
      .order("id");
  }
}
