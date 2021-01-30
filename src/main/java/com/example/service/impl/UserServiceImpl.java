package com.example.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.example.model.User;
import com.example.model.TmpUser;
import com.example.repository.UserRepository;
import com.example.repository.TmpUserRepository;
import com.example.service.UserService;
import com.example.enums.TmpUserStatusEnum;

@Service("UserService")
public class UserServiceImpl implements UserService {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final UserRepository userRepository;
  private final TmpUserRepository tmpUserRepository;

  public UserServiceImpl(
      BCryptPasswordEncoder bCryptPasswordEncoder,
      UserRepository userRepository,
      TmpUserRepository tmpUserRepository) {
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.userRepository = userRepository;
    this.tmpUserRepository = tmpUserRepository;
  }

  /**
   * ユーザー名からユーザーを取得
   * @param name ユーザー名
   * @return User
   */
  public User findArticleUserByName(String name) {
    return userRepository.findArticleUserByName(name);
  }
  
  /**
   * メールアドレスからユーザーを取得
   * @param email メールアドレス
   * @return User
   */
  public User findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  /**
   * ユーザー名からユーザーを取得
   * @param name ユーザー名
   * @return User
   */
  public User findUserByName(String name) {
    return userRepository.findByName(name);
  }

  /**
   * ハッシュ値から仮登録の情報を取得
   * @param hash ハッシュ
   * @return TmpUser
   */
  public TmpUser findTmpUserByHash(String hash) {
    return tmpUserRepository.findByHashAndStatus(hash, 0);
  }

  /**
   * 仮登録またはパスワードの再設定、仮登録テーブルのステータスを設定
   * メールアドレスがあれば更新、なければ新規作成
   * @param email メールアドレス
   * @return User
   */
  public TmpUser saveTmpUser(String email, int status) {
    TmpUser entity = tmpUserRepository.findByEmail(email);
    String hash = RandomStringUtils.randomAlphanumeric(50).toLowerCase();
    if (entity != null) {
      entity.setHash(hash);
      entity.setStatus(status);
      entity.setUpdatedAt(new Date());
      return tmpUserRepository.save(entity);
    }

    TmpUser tmpUser = new TmpUser();
    tmpUser.setEmail(email);
    tmpUser.setHash(hash);
    tmpUser.setStatus(status);
    tmpUser.setCreatedAt(new Date());
    tmpUser.setUpdatedAt(new Date());
    return tmpUserRepository.save(tmpUser);
  }

  /**
   * 仮登録テーブルを無効化し、ユーザーを本登録する
   * 仮ユーザーかユーザーテーブルのどちらかこけたらロールバック
   * @param hash ハッシュ
   * @return User
   */
  @Transactional
  public void registUser(User user, String hash) {
    this.registTmpUser(hash);
    this.createUser(user);
  }

  /**
   * 仮登録を登録済みにし、ハッシを無効化する
   * @param hash ハッシュ
   * @return User
   */
  public void registTmpUser(String hash) {
    int registStatus = TmpUserStatusEnum.REGISTERED.getStatus();
    Date currentTimestamp = new Date();
    tmpUserRepository.updateTmpUserStatus(hash, registStatus, currentTimestamp);
  }

  /**
   * ユーザ登録
   * @param user ユーザ情報
   * @return User
   */
  public User createUser(User user) {
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    user.setCreatedAt(new Date());
    user.setUpdatedAt(new Date());
    return userRepository.save(user);
  }

}