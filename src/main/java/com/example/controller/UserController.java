package com.example.controller;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import javax.validation.Valid;

import com.example.model.User;
import com.example.model.Article;
import com.example.model.view.ArticleView;
import com.example.model.view.ArticleDetailView;
import com.example.service.UserService;
import com.example.service.ArticleService;
import com.example.service.impl.UserDetailsImpl;
import com.example.utils.Pager;
import com.example.utils.MarkDown;
import com.example.enums.ArticleStatusEnum;
import com.example.enums.TemplateEnum;
import com.example.exception.NotFoundException;

@Controller
@RequestMapping("/")
public class UserController {
  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;
  private final ArticleService articleService;

  public UserController(UserService userService, ArticleService articleService) {
    this.userService = userService;
    this.articleService = articleService;
  }

  @GetMapping("/{userName}")
  public String user(
      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
      @PathVariable String userName,
      @RequestParam(name = "page", defaultValue = "1") int page,
      Model model) {
    Long loginUserId = null;
    if (userDetailsImpl != null) {
      loginUserId = userDetailsImpl.getId();
    }

    // ユーザ情報を取得
    User user = userService.findArticleUserByName(userName);
    if (user == null) {
      throw new NotFoundException();
    }

    // 最新記事
    Page<ArticleView> articleList = articleService.latestListByUserName(loginUserId, userName, page);
    Pager pager = new Pager(articleList.getNumber(), articleList.getTotalPages(), articleList.isFirst(), articleList.isLast());

    model.addAttribute("user", user);
    model.addAttribute("pager", pager);
    model.addAttribute("articleList", articleList.getContent());
    return TemplateEnum.USERARTICLELIST.getTemplate();
  }

  @GetMapping("/{userName}/article/{hash}")
  public String article(
      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
      @PathVariable String userName,
      @PathVariable String hash,
      Model model) {
    Long loginUserId = null;
    if (userDetailsImpl != null) {
      loginUserId = userDetailsImpl.getId();
    }

    ArticleDetailView article = articleService.findArticleDetail(loginUserId, userName, hash);
    if (article == null) {
      throw new NotFoundException();
    }

    MarkDown markDown = new MarkDown(article.getArticleDetail());
    article.setArticleDetail(markDown.getHtml());

    model.addAttribute("article", article);
    return TemplateEnum.DETAIL.getTemplate();
  }

}