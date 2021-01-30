package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.BindingResult;

import java.security.Principal;

import com.example.model.User;
import com.example.model.TmpUser;
import com.example.form.RegistForm;
import com.example.form.GroupOrderForm;
import com.example.service.UserService;
import com.example.service.MailSenderService;
import com.example.enums.TemplateEnum;
import com.example.enums.TmpUserStatusEnum;
import com.example.exception.NotFoundException;

@Controller
@RequestMapping("/setting")
public class SettingController {
  private static final Logger logger = LoggerFactory.getLogger(SettingController.class);

  private final UserService userService;
  private final MailSenderService mailSenderService;

  public SettingController(UserService userService, MailSenderService mailSenderService) {
    this.userService = userService;
    this.mailSenderService = mailSenderService;
  }

  /**
   * GETパラメーターのハッシュが正しいか確認
   * @param hash ハッシュ
   * @return TmpUser
   */
  public TmpUser checkHashTmpUser(String hash){
    if (hash.isEmpty()) {
      return null;
    }
    TmpUser findTmpUser = userService.findTmpUserByHash(hash);
    return findTmpUser;
  }

  // 仮登録のハッシュ値が正しければ本登録画面を表示
  @GetMapping("/regist")
  public String regist(
      Principal principal,
      RegistForm registForm,
      @RequestParam(name = "token", required = true) String hash,
      Model model) {
    if (principal != null) {
      return "redirect:/";
    }

    TmpUser findTmpUser = this.checkHashTmpUser(hash);
    if (findTmpUser == null) {
      throw new NotFoundException();
    }

    model.addAttribute("hash", hash);

    return TemplateEnum.REGIST.getTemplate();
  }

  // ユーザー本登録処理
  @PostMapping("/regist")
  public String registPost(
      Principal principal,
      @Validated(GroupOrderForm.class) RegistForm registForm,
      BindingResult bindingResult,
      @RequestParam(name = "token", required = true) String hash,
      Model model) {
    if (principal != null) {
      return "redirect:/";
    }

    TmpUser findTmpUser = this.checkHashTmpUser(hash);
    if (findTmpUser == null) {
      throw new NotFoundException();
    }

    model.addAttribute("hash", hash);

    boolean isError = false;
    // フォームのエラーチェック
    if (bindingResult.hasErrors()) {
      isError = true;
    }
    // 同名ユーザーのチェック
    if (!registForm.getName().isEmpty()) {
      if (userService.findUserByName(registForm.getName()) != null){
        isError = true;
        model.addAttribute("existUserError", "このユーザー名はすでに存在します");
      }
    }
    if (isError) {
      return TemplateEnum.REGIST.getTemplate();
    }

    User user = new User();
    // 仮登録情報からメールアドレスを取得
    user.setEmail(findTmpUser.getEmail());
    user.setName(registForm.getName());
    user.setPassword(registForm.getPassword());
    try {
      // 本登録を行う
      userService.registUser(user, hash);
      mailSenderService.registMail(user.getEmail());
    } catch (Exception e) {
      logger.error(e.getMessage());
      model.addAttribute("error", "システムエラーが発生しました");
      return TemplateEnum.REGIST.getTemplate();
    }

    return TemplateEnum.REGISTDONE.getTemplate();
  }

}