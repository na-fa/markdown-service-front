package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import org.springframework.security.core.Authentication;

import com.example.model.Article;
import com.example.service.ArticleService;
import com.example.model.view.ArticleDetailView;
import com.example.model.User;
import com.example.utils.MarkDown;
import com.example.enums.ArticleStatusEnum;
import com.example.enums.TemplateEnum;
import com.example.exception.NotFoundException;

@Controller
@RequestMapping("/preview")
public class PreviewController {
  private static final Logger logger = LoggerFactory.getLogger(PreviewController.class);

  private final ArticleService articleService;

  public PreviewController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @GetMapping("/{hash}")
  public String preview(
      @PathVariable String hash,
      Principal principal,
      Model model) {
    if (principal == null) {
      return "redirect:/";
    }

    ArticleDetailView article = articleService.findArticleDraftDetail(principal.getName(), hash);
    if (article == null) {
      throw new NotFoundException();
    }

    MarkDown markDown = new MarkDown(article.getArticleDetail());
    article.setArticleDetail(markDown.getHtml());

    model.addAttribute("article", article);
    return TemplateEnum.DETAIL.getTemplate();
  }
}