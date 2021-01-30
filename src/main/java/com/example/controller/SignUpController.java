package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.form.SignUpForm;
import com.example.service.UserService;
import com.example.service.MailSenderService;
import com.example.enums.TemplateEnum;
import com.example.enums.TmpUserStatusEnum;

@Controller
@RequestMapping("/signup")
public class SignUpController {
  private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

  private final UserService userService;
  private final MailSenderService mailSenderService;

  public SignUpController(UserService userService, MailSenderService mailSenderService) {
    this.userService = userService;
    this.mailSenderService = mailSenderService;
  }

  @GetMapping
  public String signup(
      Principal principal,
      SignUpForm signUpForm) {
    if (principal != null) {
      return "redirect:/";
    }

    return TemplateEnum.SIGNUP.getTemplate();
  }

  // 仮登録処理
  @PostMapping
  public String signupPost(
      Principal principal,
      @Validated SignUpForm signUpForm,
      BindingResult bindingResult,
      Model model) {
    if (principal != null) {
      return "redirect:/";
    }
    if (bindingResult.hasErrors()) {
      return TemplateEnum.SIGNUP.getTemplate();
    }

    User findUser = userService.findUserByEmail(signUpForm.getEmail());
    if (findUser != null) {
      model.addAttribute("error", "このメールアドレスはすでに存在します");
      return TemplateEnum.SIGNUP.getTemplate();
    }

    try {
      // 仮登録を行う
      int temporaryStatus = TmpUserStatusEnum.TEMPORARY.getStatus();
      TmpUser saveTmpUser = userService.saveTmpUser(signUpForm.getEmail(), temporaryStatus);
      mailSenderService.tmpRegistMail(saveTmpUser.getEmail(), saveTmpUser.getHash());
    } catch (Exception e) {
      logger.error(e.getMessage());
      model.addAttribute("error", "システムエラーが発生しました");
      return TemplateEnum.SIGNUP.getTemplate();
    }

    return TemplateEnum.SIGNUPDONE.getTemplate();
  }

}