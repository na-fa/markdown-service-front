package com.example.form;

import org.apache.commons.lang3.StringUtils;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Email;

public class SignUpForm {

  @NotBlank(message = "メールアドレスを入力してください")
  @Size(max = 100, message = "100字以内で入力してください")
  @Email(message = "メールアドレスの形式が違います")
  private String email;

  public SignUpForm(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = StringUtils.strip(email);
  }

  @Override
  public String toString() {
    return "SignUpForm{" +
      ", email='" + email + '\'' +
    '}';
  }
}