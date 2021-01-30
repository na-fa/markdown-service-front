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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.model.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
  @Query("SELECT t " +
      "FROM Tag t " +
      "WHERE t.enabled = 1 " +
      "  AND t.deletedAt IS NULL " +
      "ORDER BY t.path ASC")
  List<Tag> findAll();

  @Query("SELECT t " +
      "FROM Tag t " +
      "WHERE t.id IN :tagID " +
      "  AND t.enabled = 1 " +
      "  AND t.deletedAt IS NULL " +
      "ORDER BY t.path ASC")
  List<Tag> findByIdIn(@Param("tagID") List<Integer> tagID);

  @Query("SELECT t " +
      "FROM Tag t " +
      "WHERE t.name LIKE :tagName " +
      "  AND t.enabled = 1 " +
      "  AND t.deletedAt IS NULL " +
      "ORDER BY t.path ASC")
  List<Tag> findByNameLike(@Param("tagName") String tagName);

  @Query("SELECT t " +
        "FROM Tag t " +
        "JOIN t.articleTags at " +
        "WHERE at.article.id = :articleId " +
        "  AND t.enabled = 1 " +
        "  AND t.deletedAt IS NULL " +
        "ORDER BY t.path ASC")
  List<Tag> findByArticleId(@Param("articleId") Long articleId);

  @Query("SELECT t " +
        "FROM Tag t " +
        "WHERE t.path = :path " +
        "  AND t.enabled = 1 " +
        "  AND t.deletedAt IS NULL")
  Tag findByPath(@Param("path") String tagPath);
}
