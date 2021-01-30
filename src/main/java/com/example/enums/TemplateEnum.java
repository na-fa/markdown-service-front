package com.example.enums;

public enum TemplateEnum {
  TOP("index"),
  DETAIL("user/articleDetail"),
  USERARTICLELIST("user/articleList"),
  TAGLIST("tag/index"),
  TAG("tag/tag"),
  LOGIN("login/login"),
  SIGNUP("login/signup"),
  SIGNUPDONE("login/signupDone"),
  REGIST("login/regist"),
  REGISTDONE("login/registDone"),
  DASHBOARD("dashboard/index"),
  DRAFT("draft/draft"),
  DRAFTLIST("draft/draftList"),
  ABOUT("about/index"),
  NOTFOUND("error/404");

  private String template;

  TemplateEnum(String template) {
    this.template = template;
  }

  public String getTemplate() {
    return template;
  }
}
