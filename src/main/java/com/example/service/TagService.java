package com.example.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.example.model.Tag;
import com.example.model.Article;

public interface TagService {
  List<Tag> findAll();
  List<Tag> findTagByIdIn(List<Integer> tagId);
  List<Tag> findByArticleId(Long articleId);
  List<Tag> findTagByForwardName(String tagName);
  Tag findTagByPath(String tagPath);
}