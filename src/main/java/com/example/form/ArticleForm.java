package com.example.form;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

public class ArticleForm {

  @NotBlank(message = "タイトルを入力してください")
  @Size(max = 100, message = "100字以内で入力してください")
  private String title;

  @Size(min = 1, max = 5, message = "タグを5個まで選択してください")
  private Set<Integer> tags;

  @NotBlank(message = "記事内容を入力してください")
  private String detail;

  @NotNull
  @Min(0)
  @Max(1)
  private Integer status;

  public ArticleForm(
    String title,
    Set<Integer> tags,
    String detail,
    Integer status) {
    this.title = title;
    this.tags = tags;
    this.detail = detail;
    this.status = status;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = StringUtils.strip(title);
  }

  public Set<Integer> getTags() {
    return tags;
  }

  public void setTags(Set<Integer> tags) {
    this.tags = tags;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = StringUtils.strip(detail);
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "ArticleForm{" +
      "title='" + title + '\'' +
      ", tags='" + tags + '\'' +
      ", detail='" + detail + '\'' +
      ", status='" + status + '\'' +
    '}';
  }
}