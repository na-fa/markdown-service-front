package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafConfig {

  @Bean
  public TemplateEngine textTemplateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.addTemplateResolver(webTemplateResolver());
    templateEngine.addTemplateResolver(mailTemplateResolver());
    return templateEngine;
  }

  @Bean
  public ITemplateResolver webTemplateResolver() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setOrder(Integer.valueOf(1));
    templateResolver.setPrefix("templates/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setCharacterEncoding("UTF-8");
    templateResolver.setCacheable(false);
    templateResolver.setCheckExistence(true);
    return templateResolver;
  }

  @Bean
  public ITemplateResolver mailTemplateResolver() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setOrder(Integer.valueOf(2));
    templateResolver.setPrefix("templates/mail/");
    templateResolver.setSuffix(".txt");
    templateResolver.setTemplateMode(TemplateMode.TEXT);
    templateResolver.setCharacterEncoding("UTF-8");
    templateResolver.setCacheable(false);
    templateResolver.setCheckExistence(true);
    return templateResolver;
  }
}