package com.example.model.view;

import java.util.List;
import java.util.Date;

import com.example.model.Tag;

public class ArticleDetailView {
  private Long articleId;
  private String articleTitle;
  private List<Tag> articleTag;
  private String articleDetail;
  private Date articleUpdatedAt;
  private String userName;
  private boolean bookmark;

  public ArticleDetailView(
    Long articleId,
    String articleTitle,
    String articleDetail,
    Date articleUpdatedAt,
    String userName) {
    this.articleId = articleId;
    this.articleTitle = articleTitle;
    this.articleDetail = articleDetail;
    this.articleUpdatedAt = articleUpdatedAt;
    this.userName = userName;
  }

  public ArticleDetailView(
    Long articleId,
    String articleTitle,
    String articleDetail,
    Date articleUpdatedAt,
    String userName,
    boolean bookmark) {
    this.articleId = articleId;
    this.articleTitle = articleTitle;
    this.articleDetail = articleDetail;
    this.articleUpdatedAt = articleUpdatedAt;
    this.userName = userName;
    this.bookmark = bookmark;
  }

  public Long getArticleId() {
    return articleId;
  }

  public void setArticleId(Long articleId) {
    this.articleId = articleId;
  }

  public String getArticleTitle() {
    return articleTitle;
  }

  public void setArticleTitle(String articleTitle) {
    this.articleTitle = articleTitle;
  }

  public List<Tag> getArticleTag() {
    return articleTag;
  }

  public void setArticleTag(List<Tag> articleTag) {
    this.articleTag = articleTag;
  }

  public String getArticleDetail() {
    return articleDetail;
  }

  public void setArticleDetail(String articleDetail) {
    this.articleDetail = articleDetail;
  }

  public Date getArticleUpdatedAt() {
    return articleUpdatedAt;
  }

  public void setArticleUpdatedAt(Date articleUpdatedAt) {
    this.articleUpdatedAt = articleUpdatedAt;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public boolean getBookmark() {
    return bookmark;
  }

  public void setBookmark(boolean bookmark) {
    this.bookmark = bookmark;
  }

  @Override
  public String toString() {
    return "ArticleDetailView{" +
      "articleId='" + articleId + '\'' +
      ", articleTitle='" + articleTitle + '\'' +
      ", articleTag='" + articleTag + '\'' +
      ", articleDetail='" + articleDetail + '\'' +
      ", articleUpdatedAt='" + articleUpdatedAt + '\'' +
      ", userName='" + userName + '\'' +
      ", bookmark='" + bookmark + '\'' +
    '}';
  }
}