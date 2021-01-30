package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.model.Article;
import com.example.model.Bookmark;
import com.example.service.ArticleService;
import com.example.service.BookmarkService;
import com.example.service.impl.UserDetailsImpl;

@RestController
@RequestMapping(path="/api")
public class ApiController {
  private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

  private final ArticleService articleService;
  private final BookmarkService bookmarkService;

  public ApiController(ArticleService articleService, BookmarkService bookmarkService) {
    this.articleService = articleService;
    this.bookmarkService = bookmarkService;
  }

  @PostMapping("/bookmark")
  public ResponseEntity<Void> bookmark(
    @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
    @RequestParam(name = "userName") String userName,
    @RequestParam(name = "articleHash") String articleHash) {
    if (userDetailsImpl == null) {
      return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
    }
    // 自身の記事はブックマークしないようにする
    if (userDetailsImpl.getName().equals(userName)) {
      return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
    }

    Article article = articleService.findArticle(userName, articleHash);
    if (article == null) {
      return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
    }

    Bookmark bookmark = new Bookmark();
    bookmark.setArticle(article);
    bookmark.setUser(userDetailsImpl.getUser());

    try {
      bookmarkService.save(bookmark);
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new ResponseEntity<Void>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    return new ResponseEntity<Void>(HttpStatus.OK);
  }

}