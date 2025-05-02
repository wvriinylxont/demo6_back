package com.example.demo.security;

import com.example.demo.dao.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;

import java.io.*;

// 로그인에 실패하면 "로그인에 몇회 실패했습니다"라고 출력
// 로그인에 5번 실패하면 계정을 블록한 다음 "계정이 블록되었습니다"라고 출력
@Component
public class Demo6AuthenticationFailureHandler implements AuthenticationFailureHandler {
  @Autowired
  private MemberDao memberDao;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    // ※ 로그인에 실패한 경우
    // 1. 로그인 시도한 아이디를 가져와서
    // 2. db에서 사용자 정보를 읽어온다
    // 3. 로그인 실패 횟수가 3회이하라면 로그인 실패 횟수 증가  -> "로그인에 몇회 실패했습니다"
    // 4. 로그인 실패 횟수가 4회라면 5회로 증가시키고 계정 블록 -> "로그인에 5회 실패해 계정이 블록되었습니다"

    // ※ 계정이 이미 블록된 경우
    // 5. "블록된 계정입니다"


  }
}
