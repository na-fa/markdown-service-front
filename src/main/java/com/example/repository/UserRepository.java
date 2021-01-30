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

import com.example.model.User;
import com.example.model.TmpUser;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  List<User> findAll();

  @Query("SELECT u " +
        "FROM User u " +
        "WHERE u.name = :userName " +
        "  AND u.deletedAt IS NULL")
  User findArticleUserByName(@Param("userName") String userName);

  User findByEmail(String email);
  User findByName(String name);
}
