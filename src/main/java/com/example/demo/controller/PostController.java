package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.service.*;
import io.swagger.v3.oas.annotations.*;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.annotation.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.security.*;
import java.util.*;

// @Validated는 스프링 검증, 없으면 자바 표준 검증 수행. 스프링 검증이 좀 더 간략화
@Validated
@RestController
public class PostController {
  @Autowired
  private PostService service;

  @Operation(summary="페이징", description="기본 페이지번호 1, 페이지크기 10으로 페이징")
  @GetMapping("/posts")
  public ResponseEntity<PostDto.Pages> findAll(@RequestParam(defaultValue="1") int pageno, @RequestParam(defaultValue="10") int pagesize) {
    return ResponseEntity.ok(service.findAll(pageno, pagesize));
  }

  @Operation(summary="글읽기", description="글읽기")
  @GetMapping("/posts/post")
  public ResponseEntity<Map<String,Object>> findByPno(@RequestParam int pno, Principal principal) {
    String loginId = principal==null? null : principal.getName();
    return ResponseEntity.ok(service.findByPno(pno, loginId));
  }

  @Operation(summary="글쓰기")
  @Secured("ROLE_USER")
  @PostMapping("/posts/new")
  public ResponseEntity<Post> write(@ModelAttribute @Valid PostDto.Write dto, BindingResult br, Principal principal) {
    Post post = service.write(dto, principal.getName());
    return ResponseEntity.ok(post);
  }

  @Secured("ROLE_USER")
  @PutMapping("/posts/post")
  @Operation(summary="글변경", description="글번호로 제목과 내용 변경")
  public ResponseEntity<String> update(@ModelAttribute @Valid PostDto.Update dto, BindingResult br, Principal principal) {
    service.update(dto, principal.getName());
    return ResponseEntity.ok("글을 변경했습니다");
  }

  @Secured("ROLE_USER")
  @DeleteMapping("/posts/post")
  @Operation(summary="삭제")
  public ResponseEntity<String> delete(@RequestParam @NotNull Integer pno, Principal principal) {
    service.delete(pno, principal.getName());
    return ResponseEntity.ok("글을 삭제했습니다");
  }

  @Secured("ROLE_USER")
  @PutMapping("/posts/good")
  @Operation(summary="글추천", description="이미 추천한 글 재추천 불가")
  public ResponseEntity<Integer> 추천(@RequestParam @NotNull Integer pno, Principal principal) {
    int newGoodCnt = service.추천(pno, principal.getName());
    return ResponseEntity.ok(newGoodCnt);
  }
}









