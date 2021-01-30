package com.example.form;

import org.apache.commons.lang3.StringUtils;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.AssertTrue;

import com.example.form.ValidGroupForm.*;

public class RegistForm {

  @NotBlank(message = "ユーザー名を入力してください", groups = First.class)
  @Pattern(regexp = "^[a-z0-9_-]+$", message = "ユーザ名は半角英数字及び_, -のみ利用可能です", groups = First.class)
  @Size(max = 100, message = "ユーザ名は100文字以内で入力してください", groups = Second.class)
  private String name;

  @Size(min = 6, max = 100, message = "パスワードは6文字以上で入力してください", groups = First.class)
  private String password;

  private String password2;

  @AssertTrue(message = "確認用パスワードを入力してください", groups = Second.class)
  public boolean isConfirmPasswordEmpty(){
    // 最初のパスワードは@Sizeでバリデーションする
    if(password.isEmpty()) {
      return true;
    }

    // passwordが入力されている時、password2をチェックする
    if(password2.isEmpty()){
        return false;
    }

    return true;
  }

  @AssertTrue(message = "パスワードが一致しません", groups = Third.class)
  public boolean isEqualsPassword() {
    if (password.equals(password2)) {
      return true;
    }
    return false;
  }

  public RegistForm(
    String name,
    String password,
    String password2) {
    this.name = name;
    this.password = password;
    this.password2 = password2;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = StringUtils.strip(name);
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = StringUtils.strip(password);
  }

  public String getPassword2() {
    return password2;
  }

  public void setPassword2(String password2) {
    this.password2 = StringUtils.strip(password2);
  }

  @Override
  public String toString() {
    return "RegistForm{" +
      ", name='" + name + '\'' +
      ", password='" + password + '\'' +
      ", password2='" + password2 + '\'' +
    '}';
  }
}