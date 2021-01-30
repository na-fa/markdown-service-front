package com.example.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Index;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "t_user", indexes = {
  @Index(name = "ui_user_name",  columnList="name", unique = true),
  @Index(name = "ui_user_email",  columnList="email", unique = true)
})
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", length = 191, nullable = false)
  @NotBlank
  private String name;

  @Column(name = "email", length = 191, nullable = false)
  @Email
  @NotBlank
  private String email;

  @JsonProperty(access = Access.WRITE_ONLY)
  @Column(name = "password", length = 255, nullable = false)
  @NotBlank
  private String password;

  @CreatedDate
  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  @Column(name="deleted_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date deletedAt;

  public User() {
  }

  public User(
    Long id,
    String name,
    String email,
    String password,
    Date createdAt,
    Date updatedAt,
    Date deletedAt) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public Date getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(Date deletedAt) {
    this.deletedAt = deletedAt;
  }

  @Override
  public String toString() {
    return "User{" +
      "id='" + id + '\'' +
      ", name='" + name + '\'' +
      ", email='" + email + '\'' +
      ", password='" + password + '\'' +
      ", created_at='" + createdAt + '\'' +
      ", updated_at='" + updatedAt + '\'' +
      ", deleted_at='" + deletedAt + '\'' +
    '}';
  }
}