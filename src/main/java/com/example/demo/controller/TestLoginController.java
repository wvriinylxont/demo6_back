package com.example.demo.controller;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

@Controller
public class TestLoginController {
  @GetMapping("/login")
  public void login() {
  }
}
