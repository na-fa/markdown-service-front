package com.example.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "m_tag", indexes = {@Index(name = "ui_tag_path",  columnList="path", unique = true)})
public class Tag {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private int id;

  @Column(name = "path", length = 15, nullable = false)
  private String path;

  @Column(name = "name", length = 15, nullable = false)
  private String name;

  @JsonIgnore
  @Column(name = "enabled", length = 1, columnDefinition = "tinyint(1) default '1'", nullable = false)
  private int enabled;

  @JsonIgnore
  @CreatedDate
  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @JsonIgnore
  @LastModifiedDate
  @Column(name = "updated_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  @JsonIgnore
  @Column(name="deleted_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date deletedAt;

  // 循環参照させないために@JsonIgnoreが必要
  @JsonIgnore
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tag")
  private List<ArticleTag> articleTags;

  public Tag() {
  }

  public Tag(
    int id,
    String path,
    String name,
    int enabled,
    Date createdAt,
    Date updatedAt,
    Date deletedAt,
    List<ArticleTag> articleTags) {
    this.id = id;
    this.path = path;
    this.name = name;
    this.enabled = enabled;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
    this.articleTags = articleTags;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getEnabled() {
    return enabled;
  }

  public void setEnabled(int enabled) {
    this.enabled = enabled;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Date getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(Date deletedAt) {
    this.deletedAt = deletedAt;
  }

  public List<ArticleTag> getArticleTags() {
    return articleTags;
  }

  public void setArticleTags(List<ArticleTag> articleTags) {
    this.articleTags = articleTags;
  }

  @Override
  public String toString() {
    return "Tag{" +
      "id='" + id + '\'' +
      ", path='" + path + '\'' +
      ", name='" + name + '\'' +
      ", enabled='" + enabled + '\'' +
      ", created_at='" + createdAt + '\'' +
      ", updated_at='" + updatedAt + '\'' +
      ", deleted_at='" + deletedAt + '\'' +
    '}';
  }
}