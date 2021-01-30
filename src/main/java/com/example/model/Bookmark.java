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
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

@Entity
@Table(name = "t_bookmark")
public class Bookmark {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(name="fk_bookmark_user_userId"),
              name = "user_id",
              referencedColumnName = "id",
              nullable = false)
  private User user;
  
  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(name="fk_bookmark_article_articleId"),
              name = "article_id",
              referencedColumnName = "id",
              nullable = false)
  private Article article;

  public Bookmark() {
  }

  public Bookmark(
    User user,
    Article article) {
    this.user = user;
    this.article = article;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
  
  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  @Override
  public String toString() {
    return "Bookmark{" +
      "user='" + user + '\'' +
      "article'=" + article + '\'' +
    '}';
  }
}