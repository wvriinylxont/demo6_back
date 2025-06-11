package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.service.*;
import io.swagger.v3.oas.annotations.*;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.access.annotation.*;
import org.springframework.validation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.*;
import java.util.*;

// ↓ 이게 있으면 스프링 검증
@Validated
@RestController
public class CommentController {
    // 댓글 CRUD : Create, Delete 담당. Read는 PostController에서 Update는 없다
    @Autowired
    private CommentService service;

    // 인증(authentication) : 신원을 확인(로그인) -> 401
    // https는 상호 인증한다 -> 브라우저가 서버의 공인 인증서를 확인
    // 인가(authorization) : 인증 후 권한을 확인 -> 403
    @Operation(summary="댓글 작성", description="댓글을 작성하면 글의 모든 댓글을 출력")
    @Secured("ROLE_USER")
    @PostMapping("/api/comments/new")
    // 독립적으로 있다면 List<Comment>
    // @RequestBody
    // @RequestBody + HTTP 상태코드 -> ResponseEntity
    // BindingResult br 검증 결과
    public ResponseEntity<List<Comment>> write(@ModelAttribute @Valid CommentDto.Create dto, BindingResult br , Principal principal) {
        // PostRead <- Comments, CommentWrite. CommentWrite에서 댓글을 작성하면 Comments를 갱신
        List<Comment> comments = service.write(dto, principal.getName());
        return ResponseEntity.ok(comments);
    }

    @Operation(summary="댓글 삭제", description="댓글을 작성하면 글의 모든 댓글을 출력")
    @Secured("ROLE_USER")
    @DeleteMapping("/api/comments")
    public ResponseEntity<List<Comment>> delete(@ModelAttribute @Valid CommentDto.Delete dto, BindingResult br, Principal principal) {
        List<Comment> comments =  service.delete(dto, principal.getName());
        return ResponseEntity.ok(comments);
    }
}