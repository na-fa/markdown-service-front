package com.example.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.model.Bookmark;
import com.example.model.Article;
import com.example.repository.BookmarkRepository;
import com.example.service.BookmarkService;

@Service("BookmarkService")
public class BookmarkServiceImpl implements BookmarkService {

  private final BookmarkRepository bookmarkRepository;

  public BookmarkServiceImpl(BookmarkRepository bookmarkRepository) {
    this.bookmarkRepository = bookmarkRepository;
  }

  /**
   * 登録更新処理、既にレコードがあれば削除、なければ登録
   * @param tagId タグID
   * @return tag タグ情報
   */
  public void save(Bookmark bookmark) {
    Bookmark entity = bookmarkRepository.findByUserIdAndArticleId(bookmark.getUser().getId(), bookmark.getArticle().getId());
    if (entity != null) {
      bookmarkRepository.delete(entity);
    } else {
      bookmarkRepository.save(bookmark);
    }
  }
}