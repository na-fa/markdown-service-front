package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.enums.TemplateEnum;

@Controller
@RequestMapping("/about")
public class AboutController {

  @GetMapping
  public String index() {
    return TemplateEnum.ABOUT.getTemplate();
  }

}