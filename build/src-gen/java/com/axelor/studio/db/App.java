package com.axelor.studio.db;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.EqualsInclude;
import com.axelor.db.annotations.Widget;
import com.axelor.meta.db.MetaFile;
import com.google.common.base.MoreObjects;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;

@Entity
@Cacheable
@Table(
  name = "STUDIO_APP",
  indexes = {
    @Index(
      columnList = "name"
    ),
    @Index(
      columnList = "image"
    )
  }
)
public class App extends AuditableModel {

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "STUDIO_APP_SEQ"
  )
  @SequenceGenerator(
    name = "STUDIO_APP_SEQ",
    sequenceName = "STUDIO_APP_SEQ",
    allocationSize = 1
  )
  private Long id;

  @Widget(
    translatable = true,
    title = "Name"
  )
  @NotNull
  private String name;

  @EqualsInclude
  @Widget(
    title = "Code"
  )
  @NotNull
  @Column(
    unique = true
  )
  private String code;

  @Widget(
    translatable = true,
    title = "Description"
  )
  @Lob
  @Basic(
    fetch = FetchType.LAZY
  )
  @Type(
    type = "text"
  )
  private String description;

  @Widget(
    title = "Modules contains in the app"
  )
  private String modules;

  @Widget(
    title = "Depends on"
  )
  @ManyToMany(
    fetch = FetchType.LAZY,
    cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE
    }
  )
  private Set<App> dependsOnSet;

  @Widget(
    title = "Image"
  )
  @ManyToOne(
    fetch = FetchType.LAZY,
    cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE
    }
  )
  private MetaFile image;

  @Widget(
    title = "Init data loaded"
  )
  private Boolean initDataLoaded = Boolean.FALSE;

  @Widget(
    title = "Demo data loaded"
  )
  private Boolean demoDataLoaded = Boolean.FALSE;

  @Widget(
    title = "Roles imported"
  )
  private Boolean isRolesImported = Boolean.FALSE;

  @Widget(
    title = "Installed"
  )
  private Boolean active = Boolean.FALSE;

  @Widget(
    title = "Sequence"
  )
  private Integer sequence = 0;

  @Widget(
    title = "Install order"
  )
  private Integer installOrder = 0;

  @Widget(
    title = "Language",
    selection = "select.language"
  )
  private String languageSelect;

  @Widget(
    title = "Type",
    selection = "studio.app.type.select"
  )
  private String typeSelect = "others";

  @Widget(
    title = "Version"
  )
  private String appVersion;

  @Widget(
    title = "Access config"
  )
  @OneToMany(
    fetch = FetchType.LAZY,
    mappedBy = "app",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<AccessConfig> accessConfigList;

  @Widget(
    title = "Custom"
  )
  private Boolean isCustom = Boolean.FALSE;

  @Widget(
    title = "Experimental"
  )
  private Boolean isExperimental = Boolean.FALSE;

  @OneToOne(
    fetch = FetchType.LAZY,
    mappedBy = "app",
    cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE
    }
  )
  private AppStudio appStudio;

  @Widget(
    title = "Add the application to the App Management"
  )
  private Boolean isInAppView = Boolean.FALSE;

  @OneToOne(
    fetch = FetchType.LAZY,
    mappedBy = "app",
    cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE
    }
  )
  private AppBase appBase;

  @OneToOne(
    fetch = FetchType.LAZY,
    mappedBy = "app",
    cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE
    }
  )
  private AppScript appScript;

  @Widget(
    title = "Attributes"
  )
  @Basic(
    fetch = FetchType.LAZY
  )
  @Type(
    type = "json"
  )
  private String attrs;

  public App() {
  }

  public App(String name, String code) {
    this.name = name;
    this.code = code;
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

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getModules() {
    return modules;
  }

  public void setModules(String modules) {
    this.modules = modules;
  }

  public Set<App> getDependsOnSet() {
    return dependsOnSet;
  }

  public void setDependsOnSet(Set<App> dependsOnSet) {
    this.dependsOnSet = dependsOnSet;
  }

  /**
   * Add the given {@link App} item to the {@code dependsOnSet} collection.
   *
   * @param item the item to add
   */
  public void addDependsOnSetItem(App item) {
    if (getDependsOnSet() == null) {
      setDependsOnSet(new HashSet<>());
    }
    getDependsOnSet().add(item);
  }

  /**
   * Remove the given {@link App} item from the {@code dependsOnSet} collection.
   *
   * @param item the item to remove
   */
  public void removeDependsOnSetItem(App item) {
    if (getDependsOnSet() == null) {
      return;
    }
    getDependsOnSet().remove(item);
  }

  /**
   * Clear the {@code dependsOnSet} collection.
   */
  public void clearDependsOnSet() {
    if (getDependsOnSet() != null) {
      getDependsOnSet().clear();
    }
  }

  public MetaFile getImage() {
    return image;
  }

  public void setImage(MetaFile image) {
    this.image = image;
  }

  public Boolean getInitDataLoaded() {
    return initDataLoaded == null ? Boolean.FALSE : initDataLoaded;
  }

  public void setInitDataLoaded(Boolean initDataLoaded) {
    this.initDataLoaded = initDataLoaded;
  }

  public Boolean getDemoDataLoaded() {
    return demoDataLoaded == null ? Boolean.FALSE : demoDataLoaded;
  }

  public void setDemoDataLoaded(Boolean demoDataLoaded) {
    this.demoDataLoaded = demoDataLoaded;
  }

  public Boolean getIsRolesImported() {
    return isRolesImported == null ? Boolean.FALSE : isRolesImported;
  }

  public void setIsRolesImported(Boolean isRolesImported) {
    this.isRolesImported = isRolesImported;
  }

  public Boolean getActive() {
    return active == null ? Boolean.FALSE : active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Integer getSequence() {
    return sequence == null ? 0 : sequence;
  }

  public void setSequence(Integer sequence) {
    this.sequence = sequence;
  }

  public Integer getInstallOrder() {
    return installOrder == null ? 0 : installOrder;
  }

  public void setInstallOrder(Integer installOrder) {
    this.installOrder = installOrder;
  }

  public String getLanguageSelect() {
    return languageSelect;
  }

  public void setLanguageSelect(String languageSelect) {
    this.languageSelect = languageSelect;
  }

  public String getTypeSelect() {
    return typeSelect;
  }

  public void setTypeSelect(String typeSelect) {
    this.typeSelect = typeSelect;
  }

  public String getAppVersion() {
    return appVersion;
  }

  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
  }

  public List<AccessConfig> getAccessConfigList() {
    return accessConfigList;
  }

  public void setAccessConfigList(List<AccessConfig> accessConfigList) {
    this.accessConfigList = accessConfigList;
  }

  /**
   * Add the given {@link AccessConfig} item to the {@code accessConfigList} collection.
   *
   * <p>
   * It sets {@code item.app = this} to ensure the proper relationship.
   * </p>
   *
   * @param item the item to add
   */
  public void addAccessConfigListItem(AccessConfig item) {
    if (getAccessConfigList() == null) {
      setAccessConfigList(new ArrayList<>());
    }
    getAccessConfigList().add(item);
    item.setApp(this);
  }

  /**
   * Remove the given {@link AccessConfig} item from the {@code accessConfigList} collection.
   *
   * @param item the item to remove
   */
  public void removeAccessConfigListItem(AccessConfig item) {
    if (getAccessConfigList() == null) {
      return;
    }
    getAccessConfigList().remove(item);
  }

  /**
   * Clear the {@code accessConfigList} collection.
   */
  public void clearAccessConfigList() {
    if (getAccessConfigList() != null) {
      getAccessConfigList().clear();
    }
  }

  public Boolean getIsCustom() {
    return isCustom == null ? Boolean.FALSE : isCustom;
  }

  public void setIsCustom(Boolean isCustom) {
    this.isCustom = isCustom;
  }

  public Boolean getIsExperimental() {
    return isExperimental == null ? Boolean.FALSE : isExperimental;
  }

  public void setIsExperimental(Boolean isExperimental) {
    this.isExperimental = isExperimental;
  }

  public AppStudio getAppStudio() {
    return appStudio;
  }

  public void setAppStudio(AppStudio appStudio) {
    if (getAppStudio() != null) {
      getAppStudio().setApp(null);
    }
    if (appStudio != null) {
      appStudio.setApp(this);
    }
    this.appStudio = appStudio;
  }

  public Boolean getIsInAppView() {
    return isInAppView == null ? Boolean.FALSE : isInAppView;
  }

  public void setIsInAppView(Boolean isInAppView) {
    this.isInAppView = isInAppView;
  }

  public AppBase getAppBase() {
    return appBase;
  }

  public void setAppBase(AppBase appBase) {
    if (getAppBase() != null) {
      getAppBase().setApp(null);
    }
    if (appBase != null) {
      appBase.setApp(this);
    }
    this.appBase = appBase;
  }

  public AppScript getAppScript() {
    return appScript;
  }

  public void setAppScript(AppScript appScript) {
    if (getAppScript() != null) {
      getAppScript().setApp(null);
    }
    if (appScript != null) {
      appScript.setApp(this);
    }
    this.appScript = appScript;
  }

  public String getAttrs() {
    return attrs;
  }

  public void setAttrs(String attrs) {
    this.attrs = attrs;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (this == obj) return true;
    if (!(obj instanceof App)) return false;

    final App other = (App) obj;
    if (this.getId() != null || other.getId() != null) {
      return Objects.equals(this.getId(), other.getId());
    }

    return Objects.equals(getCode(), other.getCode())
      && (getCode() != null);
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
      .add("code", getCode())
      .add("modules", getModules())
      .add("initDataLoaded", getInitDataLoaded())
      .add("demoDataLoaded", getDemoDataLoaded())
      .add("isRolesImported", getIsRolesImported())
      .add("active", getActive())
      .add("sequence", getSequence())
      .add("installOrder", getInstallOrder())
      .add("languageSelect", getLanguageSelect())
      .omitNullValues()
      .toString();
  }
}
