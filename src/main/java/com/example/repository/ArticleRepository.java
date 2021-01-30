package com.example.repository;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.model.Article;
import com.example.model.view.ArticleView;
import com.example.model.view.ArticleDetailView;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>{
  @Query("SELECT NEW com.example.model.view.ArticleView( " +
      "  a.id, a.hash, a.title, a.updatedAt, u.name) " +
      "FROM Article a " +
      "INNER JOIN a.user u " +
      "INNER JOIN a.bookmarks b " +
      "WHERE b.user.id = :userId " +
      "  AND a.status = 1 " +
      "  AND a.deletedAt IS NULL " +
      "  AND u.deletedAt IS NULL " +
      "ORDER BY a.updatedAt DESC")
  Page<ArticleView> bookmarkArticle(@Param("userId") Long userId, Pageable pageable);

  @Query("SELECT a, count(a.id) as cnt " +
      "FROM Article a " +
      "JOIN FETCH a.user u " +
      "INNER JOIN a.bookmarks b " +
      "WHERE a.status = 1 " +
      "  AND a.deletedAt IS NULL " +
      "  AND u.deletedAt IS NULL " +
      "GROUP BY a.id " +
      "ORDER BY cnt DESC, a.id DESC")
  List<Article> bookmarkArticleRanking(Pageable pageable);

  @Query("SELECT NEW com.example.model.view.ArticleDetailView( " +
        "  a.id, a.title, a.detail, a.updatedAt, u.name) " +
        "FROM Article a " +
        "INNER JOIN a.user u " +
        "WHERE a.hash = :articleHash " +
        "  AND u.name = :userName " +
        "  AND a.status = 0 " +
        "  AND a.deletedAt IS NULL " +
        "  AND u.deletedAt IS NULL")
  ArticleDetailView findArticleDraftDetail(
    @Param("userName") String userName,
    @Param("articleHash") String articleHash);

  @Query("SELECT a " +
        "FROM Article a " +
        "WHERE a.user.id = :userId " +
        "  AND a.status = 0 " +
        "  AND a.deletedAt IS NULL " +
        "ORDER BY a.updatedAt DESC")
  Page<Article> findArticleDraftList(Pageable pageable, @Param("userId") Long userId);

  @Query("SELECT a " +
        "FROM Article a " +
        "WHERE a.user.id = :userId " +
        "  AND a.hash = :articleHash " +
        "  AND a.status = 0 " +
        "  AND a.deletedAt IS NULL")
  Article findArticleDraft(@Param("userId") Long userId, @Param("articleHash") String articleHash);

  Article findByUserIdAndHashAndStatus(Long userId, String hash, int status);

  @Query("SELECT a " +
        "FROM Article a " +
        "WHERE a.user.name = :userName " +
        "  AND a.hash = :articleHash " +
        "  AND a.status = 1 " +
        "  AND a.user.deletedAt IS NULL " +
        "  AND a.deletedAt IS NULL")
  Article findByUserNameAndHash(@Param("userName") String userName, @Param("articleHash") String articleHash);
}
