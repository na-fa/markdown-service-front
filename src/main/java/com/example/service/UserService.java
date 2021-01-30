package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.model.User;
import com.example.model.TmpUser;

public interface UserService {
  User findArticleUserByName(String name);
  User findUserByEmail(String email);
  User findUserByName(String name);
  TmpUser findTmpUserByHash(String hash);
  TmpUser saveTmpUser(String email, int status);
  void registUser(User user, String hash);
  void registTmpUser(String hash);
  User createUser(User user);
}