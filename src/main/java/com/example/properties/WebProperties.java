package com.example.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConstructorBinding
@ConfigurationProperties(prefix = "web")
public class WebProperties {

  private final String baseUrl;

  private final String cdnUrl;

  private final String uploadPath;

  public WebProperties(String baseUrl, String cdnUrl, String uploadPath) {
    this.baseUrl = baseUrl;
    this.cdnUrl = cdnUrl;
    this.uploadPath = uploadPath;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public String getCdnUrl() {
    return cdnUrl;
  }

  public String getUploadPath() {
    return uploadPath;
  }
}