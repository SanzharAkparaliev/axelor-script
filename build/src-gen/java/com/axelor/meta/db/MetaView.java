package com.axelor.meta.db;

import com.axelor.auth.db.AuditableModel;
import com.axelor.auth.db.Group;
import com.axelor.db.annotations.EqualsInclude;
import com.axelor.db.annotations.NameColumn;
import com.axelor.db.annotations.Widget;
import com.axelor.studio.db.StudioApp;
import com.google.common.base.MoreObjects;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;

/**
 * This object stores the xml views.
 */
@Entity
@Cacheable
@Table(
  name = "META_VIEW",
  indexes = {
    @Index(
      columnList = "name"
    ),
    @Index(
      columnList = "title"
    ),
    @Index(
      columnList = "studio_app"
    )
  }
)
public class MetaView extends AuditableModel {

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "META_VIEW_SEQ"
  )
  @SequenceGenerator(
    name = "META_VIEW_SEQ",
    sequenceName = "META_VIEW_SEQ",
    allocationSize = 1
  )
  private Long id;

  @NotNull
  private String name;

  @NameColumn
  @NotNull
  private String title;

  @Widget(
    selection = "view.type.selection"
  )
  @NotNull
  private String type;

  @NotNull
  private Integer priority = 20;

  private String model;

  private String module;

  @Lob
  @Basic(
    fetch = FetchType.LAZY
  )
  @Type(
    type = "text"
  )
  @NotNull
  private String xml;

  private Boolean computed = Boolean.FALSE;

  @EqualsInclude
  @Column(
    unique = true
  )
  private String xmlId;

  private String helpLink;

  private Boolean extension = Boolean.FALSE;

  @ManyToMany(
    fetch = FetchType.LAZY,
    cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE
    }
  )
  @JoinTable(
    name = "meta_view_groups",
    joinColumns = @JoinColumn(
      name = "meta_view_id"
    ),
    inverseJoinColumns = @JoinColumn(
      name = "group_id"
    )
  )
  private Set<Group> groups;

  private String dependentModules;

  private String dependentFeatures;

  @Widget(
    title = "Studio App"
  )
  @ManyToOne(
    fetch = FetchType.LAZY,
    cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE
    }
  )
  private StudioApp studioApp;

  public MetaView() {
  }

  public MetaView(String name) {
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getPriority() {
    return priority == null ? 0 : priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }

  public String getXml() {
    return xml;
  }

  public void setXml(String xml) {
    this.xml = xml;
  }

  public Boolean getComputed() {
    return computed == null ? Boolean.FALSE : computed;
  }

  public void setComputed(Boolean computed) {
    this.computed = computed;
  }

  public String getXmlId() {
    return xmlId;
  }

  public void setXmlId(String xmlId) {
    this.xmlId = xmlId;
  }

  public String getHelpLink() {
    return helpLink;
  }

  public void setHelpLink(String helpLink) {
    this.helpLink = helpLink;
  }

  public Boolean getExtension() {
    return extension == null ? Boolean.FALSE : extension;
  }

  public void setExtension(Boolean extension) {
    this.extension = extension;
  }

  public Set<Group> getGroups() {
    return groups;
  }

  public void setGroups(Set<Group> groups) {
    this.groups = groups;
  }

  /**
   * Add the given {@link Group} item to the {@code groups} collection.
   *
   * @param item the item to add
   */
  public void addGroup(Group item) {
    if (getGroups() == null) {
      setGroups(new HashSet<>());
    }
    getGroups().add(item);
  }

  /**
   * Remove the given {@link Group} item from the {@code groups} collection.
   *
   * @param item the item to remove
   */
  public void removeGroup(Group item) {
    if (getGroups() == null) {
      return;
    }
    getGroups().remove(item);
  }

  /**
   * Clear the {@code groups} collection.
   */
  public void clearGroups() {
    if (getGroups() != null) {
      getGroups().clear();
    }
  }

  public String getDependentModules() {
    return dependentModules;
  }

  public void setDependentModules(String dependentModules) {
    this.dependentModules = dependentModules;
  }

  public String getDependentFeatures() {
    return dependentFeatures;
  }

  public void setDependentFeatures(String dependentFeatures) {
    this.dependentFeatures = dependentFeatures;
  }

  public StudioApp getStudioApp() {
    return studioApp;
  }

  public void setStudioApp(StudioApp studioApp) {
    this.studioApp = studioApp;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (this == obj) return true;
    if (!(obj instanceof MetaView)) return false;

    final MetaView other = (MetaView) obj;
    if (this.getId() != null || other.getId() != null) {
      return Objects.equals(this.getId(), other.getId());
    }

    return Objects.equals(getXmlId(), other.getXmlId())
      && (getXmlId() != null);
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
      .add("title", getTitle())
      .add("type", getType())
      .add("priority", getPriority())
      .add("model", getModel())
      .add("module", getModule())
      .add("computed", getComputed())
      .add("xmlId", getXmlId())
      .add("helpLink", getHelpLink())
      .add("extension", getExtension())
      .omitNullValues()
      .toString();
  }
}
