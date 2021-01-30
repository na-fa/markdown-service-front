package com.example.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.example.model.Bookmark;

public interface BookmarkService {
  void save(Bookmark bookmark);
}