package com.example.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.model.User;
import com.example.model.Article;
import com.example.model.view.ArticleView;
import com.example.service.UserService;
import com.example.service.ArticleService;
import com.example.service.impl.UserDetailsImpl;
import com.example.utils.Pager;
import com.example.enums.TemplateEnum;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
  private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

  private final UserService userService;
  private final ArticleService articleService;

  public DashboardController(UserService userService, ArticleService articleService) {
    this.userService = userService;
    this.articleService = articleService;
  }

  @GetMapping
  public String index(
    @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    @RequestParam(name = "page", defaultValue = "1") int page,
    Model model) {
    if (userDetailsImpl == null) {
      return "redirect:/";
    }
    Long loginUserId = userDetailsImpl.getId();
    String loginUserName = userDetailsImpl.getName();

    // ユーザ情報を取得
    User user = userService.findArticleUserByName(loginUserName);

    // 最新記事
    Page<ArticleView> articleList = articleService.latestListByUserName(loginUserId, loginUserName, page);
    Pager pager = new Pager(articleList.getNumber(), articleList.getTotalPages(), articleList.isFirst(), articleList.isLast());

    model.addAttribute("user", user);
    model.addAttribute("pager", pager);
    model.addAttribute("articleList", articleList.getContent());
    model.addAttribute("pathName", "index");
    return TemplateEnum.DASHBOARD.getTemplate();
  }

  @GetMapping("/bookmark")
  public String bookmark(
    @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    @RequestParam(name = "page", defaultValue = "1") int page,
    Model model) {
    if (userDetailsImpl == null) {
      return "redirect:/";
    }
    Long loginUserId = userDetailsImpl.getId();
    String loginUserName = userDetailsImpl.getName();

    // ユーザ情報を取得
    User user = userService.findArticleUserByName(loginUserName);

    // ブックマーク記事
    Page<ArticleView> articleList = articleService.bookmarkArticle(loginUserId, page);
    Pager pager = new Pager(articleList.getNumber(), articleList.getTotalPages(), articleList.isFirst(), articleList.isLast());

    model.addAttribute("user", user);
    model.addAttribute("pager", pager);
    model.addAttribute("articleList", articleList.getContent());
    model.addAttribute("pathName", "bookmark");
    return TemplateEnum.DASHBOARD.getTemplate();
  }

}