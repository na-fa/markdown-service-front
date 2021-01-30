package com.example.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;
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

import com.example.model.Tag;
import com.example.model.Article;
import com.example.model.view.ArticleView;
import com.example.repository.TagRepository;
import com.example.repository.ArticleRepository;
import com.example.service.TagService;
import com.example.service.ArticleService;
import com.example.exception.ArticleException;

@Service("TagService")
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;
  private final ArticleRepository articleRepository;

  public TagServiceImpl(TagRepository tagRepository, ArticleRepository articleRepository) {
    this.tagRepository = tagRepository;
    this.articleRepository = articleRepository;
  }

  /**
   * 全タグを取得
   * @param tagId タグID
   * @return tag タグ情報
   */
  public List<Tag> findAll() {
    List<Tag> tags = tagRepository.findAll();
    return tags;
  }
  
  /**
   * タグIDリストからタグを取得
   * @param tagId タグID
   * @return tag タグ情報
   */
  public List<Tag> findTagByIdIn(List<Integer> tagId) {
    List<Tag> tags = tagRepository.findByIdIn(tagId);
    return tags;
  }

  /**
   * 記事IDから記事に紐づくタグを取得
   * @param tagId タグID
   * @return tag タグ情報
   */
  public List<Tag> findByArticleId(Long articleId) {
    List<Tag> tags = tagRepository.findByArticleId(articleId);
    return tags;
  }

  /**
   * フォームからのタグ選択に使用、前方一致で取得
   * @param tagName タグの名前
   * @return tag タグ情報
   */
  public List<Tag> findTagByForwardName(String tagName) {
    tagName = tagName + "%";
    List<Tag> tags = tagRepository.findByNameLike(tagName);
    return tags;
  }

  /**
   * パスからタグ情報を取得する
   * @param tagPath タグのパス
   * @return tag タグ情報
   */
  public Tag findTagByPath(String tagPath) {
    Tag tag = tagRepository.findByPath(tagPath);
    return tag;
  }
}