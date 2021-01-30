package com.example.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.model.ArticleImage;
import com.example.repository.ArticleImageRepository;
import com.example.service.UploadService;

@Service("UploadService")
public class UploadServiceImpl implements UploadService {
  private final ArticleImageRepository articleImageRepository;

  public UploadServiceImpl(ArticleImageRepository articleImageRepository) {
    this.articleImageRepository = articleImageRepository;
  }

  /**
   * 画像情報を保存
   * @param userId ユーザーID
   * @return articleList 記事リスト
   */
  public ArticleImage saveImage(ArticleImage articleImage) {
    articleImage.setCreatedAt(new Date());
    return articleImageRepository.save(articleImage);
  }
}