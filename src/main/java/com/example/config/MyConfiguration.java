package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.example.properties.WebProperties;
import com.example.properties.MailProperties;

@Configuration
@EnableConfigurationProperties({MailProperties.class, WebProperties.class})
public class MyConfiguration {
}