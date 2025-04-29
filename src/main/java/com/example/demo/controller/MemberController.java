package com.example.demo.controller;

import com.example.demo.dto.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

// MVC 방식은 html로 응답 vs REST 방식은 데이터 + 상태코드로 응답
@Controller
public class MemberController {
  public ResponseEntity<?> 회원가입(MemberDto.Create dto) {
    return ResponseEntity.status(200).body(dto);
  }
}
