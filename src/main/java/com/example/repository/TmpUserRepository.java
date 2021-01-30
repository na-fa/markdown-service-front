package com.example.repository;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.TmpUser;

@Repository
public interface TmpUserRepository extends JpaRepository<TmpUser, Long> {
  TmpUser findByEmail(String email);

  TmpUser findByHashAndStatus(String hash, int status);

  // ハッシュ値からステータスを変更
  @Transactional
  @Modifying
  @Query("UPDATE TmpUser SET status = :status, updatedAt=:updatedAt WHERE hash=:hash")
  int updateTmpUserStatus(@Param("hash") String hash, @Param("status") int status, @Param("updatedAt") Date updatedAt);
}
