package com.axelor.db;

import com.axelor.db.annotations.Widget;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

@MappedSuperclass
public abstract class Model {

  @Version
  private Integer version;

  @Transient
  private transient boolean selected;

  @Widget(
    massUpdate = true
  )
  private Boolean archived;

  @Widget(
    title = "Import ID",
    copyable = false,
    readonly = true
  )
  @Column(
    unique = true
  )
  private String importId;

  @Widget(
    title = "Imported from",
    copyable = false,
    readonly = true
  )
  private String importOrigin;

  @Widget(
    copyable = false
  )
  private String processInstanceId;

  public abstract Long getId();

  public abstract void setId(Long id);

  public Integer getVersion() {
    return version;
  }

  public void setVersion(Integer version) {
    this.version = version;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  public String getImportId() {
    return importId;
  }

  public void setImportId(String importId) {
    this.importId = importId;
  }

  public String getImportOrigin() {
    return importOrigin;
  }

  public void setImportOrigin(String importOrigin) {
    this.importOrigin = importOrigin;
  }

  public String getProcessInstanceId() {
    return processInstanceId;
  }

  public void setProcessInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }
}
