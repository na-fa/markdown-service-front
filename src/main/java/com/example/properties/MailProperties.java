package com.example.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConstructorBinding
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

  private final String info;

  private final String test;

  public MailProperties(String info, String test) {
    this.info = info;
    this.test = test;
  }

  public String getInfo() {
    return info;
  }

  public String getTest() {
    return test;
  }
}