package com.example.model;

import java.util.List;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ForeignKey;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "t_article", indexes = {@Index(name = "ui_article_userId_hash",  columnList="user_id, hash", unique = true)})
public class Article {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(name="fk_article_user_userId"),
              name = "user_id",
              referencedColumnName = "id",
              nullable = false)
  private User user;

  @Column(name = "hash", length = 20, columnDefinition = "char(20)", nullable = false)
  private String hash;

  @Column(name = "title", length = 191, nullable = false)
  private String title;

  @Column(name = "detail", columnDefinition = "longtext",  nullable = false)
  private String detail;

  @Column(name = "status", length = 1, columnDefinition = "tinyint(1)", nullable = false)
  private int status;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "article")
  private List<ArticleTag> articleTags;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "article")
  private List<Bookmark> bookmarks;

  @CreatedDate
  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  @Column(name="deleted_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date deletedAt;

  public Article() {
  }

  public Article(
    Long id,
    String hash,
    String title,
    String detail,
    int status,
    Date createdAt,
    Date updatedAt,
    Date deletedAt) {
    this.id = id;
    this.hash = hash;
    this.title = title;
    this.detail = detail;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
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

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
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
    return "Article{" +
      "id='" + id + '\'' +
      ", user='" + user + '\'' +
      ", hash='" + hash + '\'' +
      ", title='" + title + '\'' +
      ", detail='" + detail + '\'' +
      ", status='" + status + '\'' +
      ", created_at='" + createdAt + '\'' +
      ", updated_at='" + updatedAt + '\'' +
      ", deleted_at='" + deletedAt + '\'' +
    '}';
  }
}