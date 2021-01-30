package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.model.ArticleTag;

@Repository
public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
  @Transactional
  @Modifying
  @Query("DELETE FROM ArticleTag at WHERE at.article.id = :articleId")
  void deleteByArticleId(@Param("articleId") Long articleId);
}
