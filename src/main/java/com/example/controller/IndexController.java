package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.model.Article;
import com.example.model.view.ArticleView;
import com.example.service.ArticleService;
import com.example.service.impl.UserDetailsImpl;
import com.example.utils.Pager;

@Controller
@RequestMapping("/")
public class IndexController {

  private final ArticleService articleService;

  public IndexController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @GetMapping
  public String index(
    @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    @RequestParam(name = "page", defaultValue = "1") int page,
    Model model) {
    Long loginUserId = null;
    if (userDetailsImpl != null) {
      loginUserId = userDetailsImpl.getId();
    }

    // 最新記事
    Page<ArticleView> articleList = articleService.latestList(loginUserId, page);
    Pager pager = new Pager(articleList.getNumber(), articleList.getTotalPages(), articleList.isFirst(), articleList.isLast());

    // ランキング記事
    List<Article> bookmarkLankingList = articleService.bookmarkRanking();

    model.addAttribute("pager", pager);
    model.addAttribute("articleList", articleList.getContent());
    model.addAttribute("bookmarkLankingList", bookmarkLankingList);
    return "index";
  }

}