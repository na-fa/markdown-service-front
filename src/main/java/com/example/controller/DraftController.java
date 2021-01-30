package com.example.controller;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

import com.example.model.Article;
import com.example.model.Tag;
import com.example.form.ArticleForm;
import com.example.service.ArticleService;
import com.example.service.TagService;
import com.example.service.impl.UserDetailsImpl;
import com.example.utils.Pager;
import com.example.enums.ArticleStatusEnum;
import com.example.enums.TemplateEnum;
import com.example.exception.NotFoundException;

@Controller
@RequestMapping("/draft")
public class DraftController {
  private static final Logger logger = LoggerFactory.getLogger(DraftController.class);

  private final ArticleService articleService;
  private final TagService tagService;

  public DraftController(ArticleService articleService, TagService tagService) {
    this.articleService = articleService;
    this.tagService = tagService;
  }

  // 下書き一覧
  @GetMapping("/list")
  public String draftList(
      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
      @RequestParam(name = "page", defaultValue = "1") int page,
      Model model) {
    if (userDetailsImpl == null) {
      return "redirect:/login";
    }

    Long userId = userDetailsImpl.getId();
    Page<Article> draftList = articleService.draftList(page, userId);
    Pager pager = new Pager(draftList.getNumber(), draftList.getTotalPages(), draftList.isFirst(), draftList.isLast());

    model.addAttribute("pager", pager);
    model.addAttribute("draftList", draftList.getContent());

    return TemplateEnum.DRAFTLIST.getTemplate();
  }

  // 下書き一覧から記事を削除(Ajax)
  @PostMapping("/list")
  public ResponseEntity<Void> draftDelete(
      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
      @RequestParam(name = "articleHash") String articleHash) {
    if (userDetailsImpl == null) {
      return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
    }

    Long userId = userDetailsImpl.getId();
    Article article = articleService.deleteArticleDraft(userId, articleHash);
    if (article == null) {
      return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<Void>(HttpStatus.OK);
    }
  }

  // 記事投稿
  @GetMapping
  public String draft(
      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
      ArticleForm articleForm,
      Model model) {
    if (userDetailsImpl == null) {
      return "redirect:/login";
    }

    return TemplateEnum.DRAFT.getTemplate();
  }

  // 記事投稿でのタグ選択(Ajax)
  @PostMapping("/tags")
  @ResponseBody
  public ResponseEntity<List<Tag>> tagPost(
      @RequestParam("q") String tagName,
      Principal principal) {
    if (principal == null) {
      return new ResponseEntity<List<Tag>>(HttpStatus.FORBIDDEN);
    }
    List<Tag> tags = tagService.findTagByForwardName(tagName);
    return new ResponseEntity<List<Tag>>(tags, HttpStatus.OK);
  }

  // 下書編集
  @GetMapping("/{hash}/edit")
  public String draftHash(
      @PathVariable String hash,
      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
      ArticleForm articleForm,
      Model model) {
    if (userDetailsImpl == null) {
      return "redirect:/login";
    }

    Long userId = userDetailsImpl.getId();
    Article article = articleService.findArticleDraft(userId, hash);
    if (article == null) {
      throw new NotFoundException();
    }
    articleForm.setTitle(article.getTitle());
    articleForm.setDetail(article.getDetail());

    // 記事に紐づくタグを取得
    List<Tag> tagList = tagService.findByArticleId(article.getId());
    Set<Integer> tags = new HashSet<Integer>();
    tagList.forEach(tag -> tags.add(tag.getId()));
    articleForm.setTags(tags);
    model.addAttribute("tagList", tagList);

    return TemplateEnum.DRAFT.getTemplate();
  }

  // 記事の投稿処理
  @PostMapping
  public String draftPost(
      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
      @Validated ArticleForm articleForm,
      BindingResult bindingResult,
      Model model) {
    if (userDetailsImpl == null) {
      return "redirect:/login";
    }

    // 選択したタグ情報を取得
    List<Tag> tagList = new ArrayList<Tag>();
    if (articleForm.getTags() != null) {
      // SetからListに変換
      List<Integer> formTags = new ArrayList<Integer>(articleForm.getTags());
      tagList = tagService.findTagByIdIn(formTags);
      model.addAttribute("tagList", tagList);
    }

    if (bindingResult.hasErrors()) {
      return TemplateEnum.DRAFT.getTemplate();
    }

    Article article = new Article();
    article.setTitle(articleForm.getTitle());
    article.setDetail(articleForm.getDetail());
    article.setStatus(articleForm.getStatus());
    article.setUser(userDetailsImpl.getUser());
    try {
      articleService.save(article, tagList);
    } catch (Exception e) {
      logger.error(e.getMessage());
      model.addAttribute("error", "システムエラーが発生しました");
      return TemplateEnum.DRAFT.getTemplate();
    }

    // 投稿ならダッシュボードに、下書きなら下書き一覧に遷移
    String redirect = "/dashboard";
    ArticleStatusEnum articleStatusEnum = ArticleStatusEnum.getByStatus(articleForm.getStatus());
    if (articleStatusEnum == ArticleStatusEnum.DRAFT) {
      redirect = "/draft/list";
    }

    return "redirect:" + redirect;
  }

  // 下書きの編集処理
  @PostMapping("/{hash}/edit")
  public String draftHashPost(
      @PathVariable String hash,
      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
      @Validated ArticleForm articleForm,
      BindingResult bindingResult,
      Model model) {
    if (userDetailsImpl == null) {
      return "redirect:/login";
    }

    // 選択したタグ情報を取得
    List<Tag> tagList = new ArrayList<Tag>();
    if (articleForm.getTags() != null) {
      // SetからListに変換
      List<Integer> formTags = new ArrayList<Integer>(articleForm.getTags());
      tagList = tagService.findTagByIdIn(formTags);
      model.addAttribute("tagList", tagList);
    }

    if (bindingResult.hasErrors()) {
      return TemplateEnum.DRAFT.getTemplate();
    }

    Article article = new Article();
    article.setHash(hash);
    article.setTitle(articleForm.getTitle());
    article.setDetail(articleForm.getDetail());
    article.setStatus(articleForm.getStatus());
    article.setUser(userDetailsImpl.getUser());
    try {
      articleService.save(article, tagList);
    } catch (Exception e) {
      logger.error(e.getMessage());
      model.addAttribute("error", "システムエラーが発生しました");
      return TemplateEnum.DRAFT.getTemplate();
    }

    // 投稿ならダッシュボードに、下書きなら下書き一覧に遷移
    String redirect = "/dashboard";
    ArticleStatusEnum articleStatusEnum = ArticleStatusEnum.getByStatus(articleForm.getStatus());
    if (articleStatusEnum == ArticleStatusEnum.DRAFT) {
      redirect = "/draft/list";
    }

    return "redirect:" + redirect;
  }

}