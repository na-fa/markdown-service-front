package com.example.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.example.model.Article;
import com.example.model.Tag;
import com.example.model.view.ArticleView;
import com.example.model.view.ArticleDetailView;

public interface ArticleService {
  Page<ArticleView> latestList(Long loginUserId, int page);
  Page<ArticleView> latestListByUserName(Long loginUserId, String userName, int page);
  Page<ArticleView> latestListByTag(Long loginUserId, int tagId, int page);
  Page<ArticleView> bookmarkArticle(Long loginUserId, int page);
  List<Article> bookmarkRanking();
  ArticleDetailView findArticleDetail(Long loginUserId, String userName, String articleHash);
  Article findArticle(String userName, String articleHash);
  ArticleDetailView findArticleDraftDetail(String userName, String articleHash);
  Page<Article> draftList(int page, Long userId);
  Article findArticleDraft(Long userId, String articleHash);
  Article deleteArticleDraft(Long userId, String articleHash);
  Article save(Article article, List<Tag> tagList);
}