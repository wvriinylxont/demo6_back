package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.service.*;
import io.swagger.v3.oas.annotations.*;
import jakarta.servlet.http.*;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.*;
import java.util.*;

// MVC 방식은 html로 응답 vs REST 방식은 데이터 + 상태코드로 응답
// @Controller는 MVC와 REST 방식을 모두 지원
// @RestController는 REST 방식만 지원(ModelAndView 리턴 불가)
@Validated
@Controller
public class MemberController {
  @Autowired
  private MemberService service;

  // http://localhost:8080/api/members/verify?code= + code;
  @PreAuthorize("isAnonymous()")
  @GetMapping("/api/members/verify")
  public ModelAndView verifyCheckcode(@RequestParam String code) {
    // 이메일에서 링크를 클릭해서 체크 코드를 확인하는 작업은 리액트와 무관
    // 응답이 ResponseEntity가 아니라 redirect가 걸려야한다
    // 리액트 앱은 그 redirect 주소를 처리할 화면을 가지고 있어야한다
    boolean result = service.verify(code);
    return new ModelAndView("redirect:http://localhost:3000/member/verifed?result=" + result);
  }

  @PreAuthorize("isAnonymous()") // 아이디확인 하려면 비로그인
  @Operation(summary= "아이디 확인", description="아이디가 사용가능한 지 확인")
  @GetMapping("/api/members/check-username")
  public ResponseEntity<String> checkUsername(@ModelAttribute @Valid MemberDto.UsernameCheck dto, BindingResult br) {
    System.out.println(dto);
    boolean result = service.checkUsername(dto);
    if(result)
      return ResponseEntity.ok("사용가능합니다");
    return ResponseEntity.status(HttpStatus.CONFLICT).body("사용중인 아이디입니다");
  }

  @PreAuthorize("isAnonymous()") // 회원가입하려면 비로그인
  @Operation(summary="회원가입", description="회원가입 및 프로필 사진 업로드")
  @PostMapping(value="/api/members/new", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Member> signup(@ModelAttribute @Valid MemberDto.Create dto, BindingResult br) {
    Member member = service.signup(dto);
    return ResponseEntity.status(200).body(member);
  }

  @PreAuthorize("isAnonymous()")
  @Operation(summary="아이디 찾기", description="가입한 이메일로 아이디를 찾는다")
  @GetMapping("/api/members/username")
  public ResponseEntity<String> searchUsername(@RequestParam @Email String email) {
    Optional<String> result = service.searchUsername(email);
    if(result.isPresent())
      return ResponseEntity.ok(result.get());
    return ResponseEntity.status(HttpStatus.CONFLICT).body("사용자를 찾을 수 없습니다");
  }

  @PreAuthorize("isAnonymous()")
  @Operation(summary="임시비밀번호 발급", description="아이디와 이메일로 임시비밀번호를 발급")
  @PutMapping("/api/members/password")
  public ResponseEntity<String> getTemporaryPassword(@ModelAttribute @Valid MemberDto.FindByPassword dto, BindingResult br) {
    boolean result = service.getTemporaryPassword(dto);
    if(result)
      return ResponseEntity.status(200).body("임시비밀번호 발급");
    return ResponseEntity.status(409).body("사용자를 찾을 수 없습니다");
  }

  @PreAuthorize("isAuthenticated()")
  @Operation(summary="비밀번호 확인", description="현재 접속 중인 사용자의 비밀번호를 재확인")
  @PutMapping("/api/members/check-password")
  public ResponseEntity<String> checkPassword(@RequestParam String password, Principal principal) {
    boolean checkSuccess = service.checkPassword(password, principal.getName());
    if(checkSuccess)
      return ResponseEntity.ok("비밀번호 확인 성공");
    return ResponseEntity.status(HttpStatus.CONFLICT).body("비밀번호 확인 실");
  }

  // 비밀번호 확인
  @PreAuthorize("isAuthenticated()")
  @Operation(summary="비밀번호 확인", description="내 정보보기를 위한 비밀번호 재확인")
  @GetMapping("/api/members/password")
  public ResponseEntity<String> checkPassword(@ModelAttribute @Valid MemberDto.CheckPassword dto, BindingResult br, Principal principal) {
    boolean result = service.checkPassword(dto, principal.getName());
    if(result)
      return ResponseEntity.status(200).body("확인 성공");
    return ResponseEntity.status(409).body("확인 실패");
  }

  // 내정보보기
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "내 정보 보기", description = "내 정보 보기")
  @GetMapping("/api/members/member")
  public ResponseEntity<MemberDto.Read> read(Principal principal) {
    MemberDto.Read dto = service.read(principal.getName());
    return ResponseEntity.ok(dto);
  }

  // 프사 변경
  @PreAuthorize("isAuthenticated()")
  @PutMapping("/api/members/profile")
  public ResponseEntity<MemberDto.Read> changeProfile(MultipartFile profile, Principal principal) {
    MemberDto.Read dto = service.changeProfile(profile, principal.getName());
    return ResponseEntity.status(200).body(dto);
  }

  // 비밀번호 변경
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "비밀번호 변경", description = "기존 비밀번호, 새 비밀번호로 비밀번호 변경")
  @PatchMapping("/api/members/password")
  public ResponseEntity<String> changePassword(@ModelAttribute @Valid MemberDto.PasswordChange dto, BindingResult br, Principal principal) {
    boolean result = service.changePassword(dto, principal.getName());
    if(result)
      return ResponseEntity.ok("비밀번호 변경");
    return ResponseEntity.status(409).body("비밀번호 변경 실패");
  }

  // 회원 탈퇴
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "회원 탈퇴", description = "로그아웃시킨 후 회원 탈퇴")
  @DeleteMapping("/api/members/member")
  public ResponseEntity<String> resign(Principal principal, HttpSession session) {
    service.resign(principal.getName());
    session.invalidate();
    return ResponseEntity.ok("회원 탈퇴");
  }
}















