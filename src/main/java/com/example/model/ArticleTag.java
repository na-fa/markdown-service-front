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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "r_article_tag")
public class ArticleTag {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(foreignKey = @ForeignKey(name="fk_articleTag_article_articleId"),
              name = "article_id",
              referencedColumnName = "id",
              nullable = false)
  private Article article;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(foreignKey = @ForeignKey(name="fk_articleTag_tag_tagId"),
              name = "tag_id",
              referencedColumnName = "id",
              nullable = false)
  private Tag tag;

  public ArticleTag() {
  }

  public ArticleTag(
    Article article,
    Tag tag) {
    this.article = article;
    this.tag = tag;
  }

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }

  public Tag getTag() {
    return tag;
  }

  public void setTag(Tag tag) {
    this.tag = tag;
  }

  @Override
  public String toString() {
    return "ArticleTag{" +
      "article='" + article + '\'' +
      ", tag='" + tag + '\'' +
    '}';
  }
}