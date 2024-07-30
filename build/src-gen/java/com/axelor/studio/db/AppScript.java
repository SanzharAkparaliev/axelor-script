package com.axelor.studio.db;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.annotations.Track;
import com.axelor.db.annotations.TrackField;
import com.axelor.db.annotations.Widget;
import com.axelor.meta.db.MetaModel;
import com.google.common.base.MoreObjects;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

@Entity
@Cacheable
@Table(
  name = "STUDIO_APP_SCRIPT",
  indexes = @Index(
    columnList = "app"
  )
)
@Track(
  fields = {
    @TrackField(
      name = "chatLimit"
    ),
    @TrackField(
      name = "model"
    )
  }
)
public class AppScript extends AuditableModel {

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "STUDIO_APP_SCRIPT_SEQ"
  )
  @SequenceGenerator(
    name = "STUDIO_APP_SCRIPT_SEQ",
    sequenceName = "STUDIO_APP_SCRIPT_SEQ",
    allocationSize = 1
  )
  private Long id;

  @OneToOne(
    fetch = FetchType.LAZY,
    cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE
    }
  )
  private App app;

  private LocalTime chatLimit;

  @OneToMany(
    fetch = FetchType.LAZY,
    cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE
    }
  )
  private List<MetaModel> model;

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

  public AppScript() {
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public App getApp() {
    return app;
  }

  public void setApp(App app) {
    this.app = app;
  }

  public LocalTime getChatLimit() {
    return chatLimit;
  }

  public void setChatLimit(LocalTime chatLimit) {
    this.chatLimit = chatLimit;
  }

  public List<MetaModel> getModel() {
    return model;
  }

  public void setModel(List<MetaModel> model) {
    this.model = model;
  }

  /**
   * Add the given {@link MetaModel} item to the {@code model} collection.
   *
   * @param item the item to add
   */
  public void addModel(MetaModel item) {
    if (getModel() == null) {
      setModel(new ArrayList<>());
    }
    getModel().add(item);
  }

  /**
   * Remove the given {@link MetaModel} item from the {@code model} collection.
   *
   * @param item the item to remove
   */
  public void removeModel(MetaModel item) {
    if (getModel() == null) {
      return;
    }
    getModel().remove(item);
  }

  /**
   * Clear the {@code model} collection.
   */
  public void clearModel() {
    if (getModel() != null) {
      getModel().clear();
    }
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
    if (!(obj instanceof AppScript)) return false;

    final AppScript other = (AppScript) obj;
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
      .add("chatLimit", getChatLimit())
      .omitNullValues()
      .toString();
  }
}
