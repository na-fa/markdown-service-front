package com.example.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.stereotype.Service;

import com.example.properties.WebProperties;
import com.example.properties.MailProperties;

@Service("MailSenderService")
public class MailSenderService {

  private final TemplateEngine templateEngine;
  private final MailSender mailSender;
  private final MailProperties mailProperties;
  private final WebProperties webProperties;

  public MailSenderService(
    TemplateEngine templateEngine,
    MailSender mailSender,
    WebProperties webProperties,
    MailProperties mailProperties) {
    this.templateEngine = templateEngine;
    this.mailSender = mailSender;
    this.webProperties = webProperties;
    this.mailProperties = mailProperties;
  }

  private void sendEmail(SimpleMailMessage message) {
    mailSender.send(message);
  }

  // 仮登録完了メール
  public void tmpRegistMail(String to, String hash) {
    // 会員情報入力のURLを生成
    StringBuilder urlTmp = new StringBuilder(webProperties.getBaseUrl());
    urlTmp.append("/setting/regist?token=");
    urlTmp.append(hash);
    String url = urlTmp.toString();

    // テンプレートに埋め込むデータを設定
    Context context = new Context();
    context.setVariable("email", to);
    context.setVariable("url", url);
    String text = this.templateEngine.process("tmpRegist", context);

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(mailProperties.getInfo());
    message.setTo(to);
    message.setSubject("会員登録手続きのお願い");
    message.setText(text);
    this.sendEmail(message);
  }

  // 本登録完了メール
  public void registMail(String to) {
    // テンプレートに埋め込むデータを設定
    Context context = new Context();
    context.setVariable("url", webProperties.getBaseUrl());
    String text = this.templateEngine.process("regist", context);

    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(mailProperties.getInfo());
    message.setTo(to);
    message.setSubject("登録完了のご案内");
    message.setText(text);
    this.sendEmail(message);
  }

}
