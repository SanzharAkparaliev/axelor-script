package com.axelor.meta.db;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.Widget;
import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * This object stores the models.
 */
@Entity
@Cacheable
@Table(
  name = "META_MODEL",
  indexes = @Index(
    columnList = "name"
  )
)
@Track(
  fields = @TrackField(
    name = "generate"
  )
)
public class MetaModel extends AuditableModel {

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "META_MODEL_SEQ"
  )
  @SequenceGenerator(
    name = "META_MODEL_SEQ",
    sequenceName = "META_MODEL_SEQ",
    allocationSize = 1
  )
  private Long id;

  @Widget(
    title = "Name"
  )
  @NotNull
  private String name;

  @Widget(
    title = "Package"
  )
  @NotNull
  private String packageName;

  @Widget(
    title = "Table"
  )
  private String tableName;

  @Widget(
    title = "Fields"
  )
  @OneToMany(
    fetch = FetchType.LAZY,
    mappedBy = "metaModel",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<MetaField> metaFields;

  @Widget(
    title = "Fullname"
  )
  private String fullName;

  @Widget(
    massUpdate = true
  )
  private Boolean generate = Boolean.FALSE;

  public MetaModel() {
  }

  public MetaModel(String name) {
    this.name = name;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public List<MetaField> getMetaFields() {
    return metaFields;
  }

  public void setMetaFields(List<MetaField> metaFields) {
    this.metaFields = metaFields;
  }

  /**
   * Add the given {@link MetaField} item to the {@code metaFields} collection.
   *
   * <p>
   * It sets {@code item.metaModel = this} to ensure the proper relationship.
   * </p>
   *
   * @param item the item to add
   */
  public void addMetaField(MetaField item) {
    if (getMetaFields() == null) {
      setMetaFields(new ArrayList<>());
    }
    getMetaFields().add(item);
    item.setMetaModel(this);
  }

  /**
   * Remove the given {@link MetaField} item from the {@code metaFields} collection.
   *
   * @param item the item to remove
   */
  public void removeMetaField(MetaField item) {
    if (getMetaFields() == null) {
      return;
    }
    getMetaFields().remove(item);
  }

  /**
   * Clear the {@code metaFields} collection.
   */
  public void clearMetaFields() {
    if (getMetaFields() != null) {
      getMetaFields().clear();
    }
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public Boolean getGenerate() {
    return generate == null ? Boolean.FALSE : generate;
  }

  public void setGenerate(Boolean generate) {
    this.generate = generate;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (this == obj) return true;
    if (!(obj instanceof MetaModel)) return false;

    final MetaModel other = (MetaModel) obj;
    if (this.getId() != null || other.getId() != null) {
      return Objects.equals(this.getId(), other.getId());
    }

    return false;
  }

  @Override
  public int hashCode() {
    return 31;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
     .add("id", getId())
      .add("name", getName())
      .add("packageName", getPackageName())
      .add("tableName", getTableName())
      .add("fullName", getFullName())
      .add("generate", getGenerate())
      .omitNullValues()
      .toString();
  }
}
