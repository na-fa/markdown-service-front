package com.example.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
class ExceptionHandlerController {

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "404 not found")
  @ExceptionHandler({ NotFoundException.class })
  public ModelAndView errorHandler404(Exception e, HttpServletRequest request) {
    ModelAndView mav = new ModelAndView();
    System.out.println("404Exception!!!");
    mav.setViewName("error/404");
    return mav;
  }
}