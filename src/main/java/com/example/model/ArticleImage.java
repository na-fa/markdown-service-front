package com.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.ForeignKey;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "t_article_image", indexes = {@Index(name = "ui_articleImage_path_uuid",  columnList="path, uuid", unique = true)})
public class ArticleImage {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(name="fk_articleImage_user_userId"),
              name = "user_id",
              referencedColumnName = "id",
              nullable = false)
  private User user;

  @Column(name = "path", length = 6, columnDefinition = "char(6)", nullable = false)
  private String path;

  @Column(name = "uuid", length = 36, columnDefinition = "char(36)", nullable = false)
  private String uuid;

  @Column(name = "extension", length = 4, nullable = false)
  private String extension;

  @CreatedDate
  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  public ArticleImage() {
  }

  public ArticleImage(
    Long id,
    String path,
    String uuid,
    String extension,
    Date createdAt) {
    this.id = id;
    this.path = path;
    this.uuid = uuid;
    this.extension = extension;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "ArticleImage{" +
      "id='" + id + '\'' +
      ", user='" + user + '\'' +
      ", path='" + path + '\'' +
      ", uuid='" + uuid + '\'' +
      ", extension='" + extension + '\'' +
      ", created_at='" + createdAt + '\'' +
    '}';
  }
}