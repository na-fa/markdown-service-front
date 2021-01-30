package com.example.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Article;
import com.example.model.Tag;
import com.example.model.ArticleTag;
import com.example.model.view.ArticleView;
import com.example.model.view.ArticleDetailView;
import com.example.repository.ArticleRepository;
import com.example.repository.TagRepository;
import com.example.repository.ArticleTagRepository;
import com.example.persistence.ArticlePersistence;
import com.example.service.ArticleService;
import com.example.exception.ArticleException;

@Service("ArticleService")
public class ArticleServiceImpl implements ArticleService {

  private final ArticleRepository articleRepository;
  private final TagRepository tagRepository;
  private final ArticleTagRepository articleTagRepository;
  private final ArticlePersistence articlePersistence;

  public ArticleServiceImpl(
      ArticleRepository articleRepository,
      TagRepository tagRepository,
      ArticleTagRepository articleTagRepository,
      ArticlePersistence articlePersistence) {
    this.articleRepository = articleRepository;
    this.tagRepository = tagRepository;
    this.articleTagRepository = articleTagRepository;
    this.articlePersistence = articlePersistence;
  }

  /**
   * 最新記事を取得
   * @param loginUserId ログインユーザーID
   * @param page ページID
   * @return articleList 記事リスト
   */
  public Page<ArticleView> latestList(Long loginUserId, int page) {
    Pageable pageable = PageRequest.of(page - 1, 30);
    Page<ArticleView> articleList = articlePersistence.latestList(loginUserId, pageable);
    
    // 記事に紐づくタグを取得
    articleList.forEach(article -> {
      List<Tag> tagList = tagRepository.findByArticleId(article.getArticleId());
      article.setArticleTag(tagList);
    });

    return articleList;
  }

  /**
   * ユーザー名から最新記事を取得
   * @param loginUserId ログインユーザーID
   * @param loginUserId 記事ユーザー名
   * @param page ページID
   * @return articleList 記事リスト
   */
  public Page<ArticleView> latestListByUserName(Long loginUserId, String userName, int page) {
    Pageable pageable = PageRequest.of(page - 1, 30);
    Page<ArticleView> articleList = articlePersistence.latestListByUserName(loginUserId, userName, pageable);
    
    // 記事に紐づくタグを取得
    articleList.forEach(article -> {
      List<Tag> tagList = tagRepository.findByArticleId(article.getArticleId());
      article.setArticleTag(tagList);
    });

    return articleList;
  }

  /**
   * タグに紐づく最新記事を取得
   * @param loginUserId ログインユーザーID
   * @param page ページID
   * @return articleList 記事リスト
   */
  public Page<ArticleView> latestListByTag(Long loginUserId, int tagId, int page) {
    Pageable pageable = PageRequest.of(page - 1, 30);
    Page<ArticleView> articleList = articlePersistence.latestListByTag(loginUserId, tagId, pageable);
    
    // 記事に紐づくタグを取得
    articleList.forEach(article -> {
      List<Tag> tagList = tagRepository.findByArticleId(article.getArticleId());
      article.setArticleTag(tagList);
    });

    return articleList;
  }

  /**
   * ブックマークしている記事を取得
   * @param loginUserId ログインユーザーID
   * @param page ページID
   * @return articleList 記事リスト
   */
  public Page<ArticleView> bookmarkArticle(Long loginUserId, int page) {
    Pageable pageable = PageRequest.of(page - 1, 30);
    Page<ArticleView> articleList = articleRepository.bookmarkArticle(loginUserId, pageable);

    // 記事に紐づくタグを取得
    articleList.forEach(article -> {
      List<Tag> tagList = tagRepository.findByArticleId(article.getArticleId());
      article.setArticleTag(tagList);
    });

    return articleList;
  }
  
  /**
   * ブックマークランキングTOP5を取得
   * @return lankingList 記事リスト
   */
  public List<Article> bookmarkRanking() {
    Pageable pageable = PageRequest.of(0, 5);
    List<Article> lankingList = articleRepository.bookmarkArticleRanking(pageable);
    return lankingList;
  }

  /**
   * ユーザー名と記事ハッシュから記事の内容を取得
   * @param loginUserId ログインユーザーID
   * @param userName 記事ユーザー名
   * @param articleHash 記事ハッシュ
   * @return article 記事情報
   */
  public ArticleDetailView findArticleDetail(Long loginUserId, String userName, String articleHash) {
    ArticleDetailView article = articlePersistence.articleDetail(loginUserId, userName, articleHash);

    // 記事に紐づくタグを取得
    if (article != null) {
      List<Tag> tagList = tagRepository.findByArticleId(article.getArticleId());
      article.setArticleTag(tagList);
    }

    return article;
  }

  /**
   * ユーザー名と記事ハッシュから記事を取得
   * @param userName 記事ユーザー名
   * @param articleHash 記事ハッシュ
   * @return article 記事情報
   */
  public Article findArticle(String userName, String articleHash) {
    Article article = articleRepository.findByUserNameAndHash(userName, articleHash);
    return article;
  }

  /**
   * プレビュー
   * ユーザー名と記事ハッシュから下書きの内容を取得
   * @param userName ユーザー名
   * @param articleHash 記事ハッシュ
   * @return article 記事情報
   */
  public ArticleDetailView findArticleDraftDetail(String userName, String articleHash) {
    ArticleDetailView article = articleRepository.findArticleDraftDetail(userName, articleHash);

    // 記事に紐づくタグを取得
    if (article != null) {
      List<Tag> tagList = tagRepository.findByArticleId(article.getArticleId());
      article.setArticleTag(tagList);
    }

    return article;
  }

  /**
   * 下書き一覧を取得
   * @param userId ユーザーID
   * @return draftList 記事リスト
   */
  public Page<Article> draftList(int page, Long userId) {
    Pageable pageable = PageRequest.of(page - 1, 30);
    Page<Article> draftList = articleRepository.findArticleDraftList(pageable, userId);
    return draftList;
  }

  /**
   * ユーザーIDと記事ハッシュから下書き記事を取得
   * @param userId ユーザーID
   * @param hash 記事ハッシュ
   * @return article 記事情報
   */
  public Article findArticleDraft(Long userId, String articleHash) {
    Article article = articleRepository.findArticleDraft(userId, articleHash);
    return article;
  }

  /**
   * ユーザーIDと記事ハッシュから下書き記事を削除
   * @param userId ユーザーID
   * @param hash 記事ハッシュ
   * @return article 記事情報
   */
  public Article deleteArticleDraft(Long userId, String articleHash) {
    // 下書きがあるかを確認
    Article article = articleRepository.findArticleDraft(userId, articleHash);
    if(article == null){
      return null;
    }
    article.setDeletedAt(new Date());
    return articleRepository.save(article);
  }

  /**
   * 記事を作成または更新
   * ハッシュがあれば更新処理、なければ新規作成
   * @param article 記事情報
   * @return article
   */
  @Transactional
  public Article save(Article article, List<Tag> tagList) {
    if (article.getHash() != null) {
      // 下書き記事が見つかれば更新
      Article entity = articleRepository.findArticleDraft(article.getUser().getId(), article.getHash());
      if (entity != null) {
        entity.setTitle(article.getTitle());
        entity.setDetail(article.getDetail());
        entity.setStatus(article.getStatus());
        entity.setUpdatedAt(new Date());
        entity = articleRepository.save(entity);

        // 記事に紐づくタグを削除
        articleTagRepository.deleteByArticleId(entity.getId());

        // 記事にタグを登録
        List<ArticleTag> articleTagList = new ArrayList<ArticleTag>();
        for (Tag tag : tagList) {
          ArticleTag articleTag = new ArticleTag();
          articleTag.setArticle(entity);
          articleTag.setTag(tag);
          articleTagList.add(articleTag);
        }
        articleTagRepository.saveAll(articleTagList);

        return entity;
      } else {
        // 更新の対象レコードが見つからなければ例外を投げる
        throw new ArticleException("update record not found.");
      }
    }

    // 小文字英数字の文字列を作成
    String hash = RandomStringUtils.randomAlphanumeric(20).toLowerCase();
    article.setHash(hash);
    article.setCreatedAt(new Date());
    article.setUpdatedAt(new Date());
    article = articleRepository.save(article);

    // 記事にタグを登録
    List<ArticleTag> articleTagList = new ArrayList<ArticleTag>();
    for (Tag tag : tagList) {
      ArticleTag articleTag = new ArticleTag();
      articleTag.setArticle(article);
      articleTag.setTag(tag);
      articleTagList.add(articleTag);
    }
    articleTagRepository.saveAll(articleTagList);

    return article;
  }
}