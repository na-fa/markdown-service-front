package com.example.model.view;

import java.util.List;
import java.util.Date;

import com.example.model.Tag;

public class ArticleView {
  private Long articleId;
  private String articleHash;
  private String articleTitle;
  private List<Tag> articleTag;
  private Date articleUpdatedAt;
  private String userName;
  private boolean bookmark;

  public ArticleView(
    Long articleId,
    String articleHash,
    String articleTitle,
    Date articleUpdatedAt,
    String userName) {
    this.articleId = articleId;
    this.articleHash = articleHash;
    this.articleTitle = articleTitle;
    this.articleUpdatedAt = articleUpdatedAt;
    this.userName = userName;
  }
  
  public ArticleView(
    Long articleId,
    String articleHash,
    String articleTitle,
    Date articleUpdatedAt,
    String userName,
    boolean bookmark) {
    this.articleId = articleId;
    this.articleHash = articleHash;
    this.articleTitle = articleTitle;
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

  public String getArticleHash() {
    return articleHash;
  }

  public void setArticleHash(String articleHash) {
    this.articleHash = articleHash;
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
    return "ArticleView{" +
      "articleId='" + articleId + '\'' +
      ", articleHash='" + articleHash + '\'' +
      ", articleTitle='" + articleTitle + '\'' +
      ", articleTag='" + articleTag + '\'' +
      ", articleUpdatedAt='" + articleUpdatedAt + '\'' +
      ", userName='" + userName + '\'' +
      ", bookmark='" + bookmark + '\'' +
    '}';
  }
}