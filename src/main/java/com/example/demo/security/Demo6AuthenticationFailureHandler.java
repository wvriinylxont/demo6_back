package com.example.demo.security;

import com.example.demo.dao.*;
import com.example.demo.util.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
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
    // 계정이 블록되어 실패 : 401(로그인 필요)
    // 아이디나 비밀번호가 틀려서 실패 : 403(권한이 필요)
    if (exception instanceof LockedException) {
      ResponseUtil.sendJsonResponse(response, 403, "블록된 계정");
    } else {
      ResponseUtil.sendJsonResponse(response, 401, "로그인 실패");
    }
  }
}
