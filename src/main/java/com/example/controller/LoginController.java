package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.security.Principal;

@Controller
@RequestMapping("/login")
public class LoginController {

  @GetMapping
  public String login(
      Principal principal,
      HttpServletRequest request,
      @RequestParam(name = "error", required = false) String error) {
    HttpSession session = request.getSession(false);
    if (error == null && session != null) {
      if (session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION") != null) {
        session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");
      }
    }

    if (principal != null) {
      return "redirect:/";
    }

    return "login/login";
  }

}