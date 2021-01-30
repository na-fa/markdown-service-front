package com.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "t_tmp_user", indexes = {
  @Index(name = "ui_tmpUser_email",  columnList="email", unique = true),
  @Index(name = "ix_tmpUser_hash",  columnList="hash", unique = false)
})
public class TmpUser {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Column(name = "email", length = 191, nullable = false)
  @Email
  @NotBlank
  private String email;

  @Column(name = "hash", length = 50, nullable = false)
  private String hash;

  @Column(name = "status", length = 1, columnDefinition = "tinyint(1)", nullable = false)
  private int status;

  @CreatedDate
  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  public TmpUser() {
  }

  public TmpUser(
    Long id,
    String email,
    String hash,
    int status,
    Date createdAt,
    Date updatedAt) {
    this.id = id;
    this.email = email;
    this.hash = hash;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }
  
  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public String toString() {
    return "TmpUser{" +
      "id='" + id + '\'' +
      ", email='" + email + '\'' +
      ", hash='" + hash + '\'' +
      ", status='" + status + '\'' +
      ", created_at='" + createdAt + '\'' +
      ", updated_at='" + updatedAt + '\'' +
    '}';
  }
}