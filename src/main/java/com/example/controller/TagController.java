package com.example.controller;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.example.model.Tag;
import com.example.model.Article;
import com.example.model.view.ArticleView;
import com.example.service.TagService;
import com.example.service.ArticleService;
import com.example.enums.TemplateEnum;
import com.example.service.impl.UserDetailsImpl;
import com.example.utils.Pager;
import com.example.exception.NotFoundException;

@Controller
@RequestMapping("/tags")
public class TagController {

  private final TagService tagService;
  private final ArticleService articleService;

  public TagController(TagService tagService, ArticleService articleService) {
    this.tagService = tagService;
    this.articleService = articleService;
  }

  @GetMapping
  public String index(Model model) {
    List<Tag> tagList = tagService.findAll();
    model.addAttribute("tagList", tagList);
    return TemplateEnum.TAGLIST.getTemplate();
  }

  @GetMapping("/{tagPath}")
  public String tag(
    @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    @PathVariable String tagPath,
    @RequestParam(name = "page", defaultValue = "1") int page,
    Model model) {
    Long loginUserId = null;
    if (userDetailsImpl != null) {
      loginUserId = userDetailsImpl.getId();
    }

    Tag tag = tagService.findTagByPath(tagPath);
    if (tag == null) {
      throw new NotFoundException();
    }

    // 最新記事
    Page<ArticleView> articleList = articleService.latestListByTag(loginUserId, tag.getId(), page);
    Pager pager = new Pager(articleList.getNumber(), articleList.getTotalPages(), articleList.isFirst(), articleList.isLast());

    // ランキング記事
    List<Article> bookmarkLankingList = articleService.bookmarkRanking();

    model.addAttribute("tag", tag);
    model.addAttribute("pager", pager);
    model.addAttribute("articleList", articleList.getContent());
    model.addAttribute("bookmarkLankingList", bookmarkLankingList);
    return TemplateEnum.TAG.getTemplate();
  }

}