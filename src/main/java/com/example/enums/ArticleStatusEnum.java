package com.example.enums;

public enum ArticleStatusEnum {
  DRAFT(0, "Draft"),
  PUBLISH(1, "Publish");

  private int status;
  private String name;

  ArticleStatusEnum(int status, String name) {
    this.status = status;
    this.name = name;
  }

  public int getStatus() {
    return status;
  }

  public String getName() {
    return name;
  }

  /**
   * ステータスが存在するかを確認して、インスタンスを返す
   * @param status 記事ステータス
   * @return ArticleStatusEnum
   */
  public static ArticleStatusEnum getByStatus(int status) {
    for (ArticleStatusEnum articleStatus : ArticleStatusEnum.values()) {
      if (articleStatus.getStatus() == status) {
        return articleStatus;
      }
    }
    return null;
  }
}
