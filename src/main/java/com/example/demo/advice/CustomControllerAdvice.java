package com.example.demo.advice;

import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class CustomControllerAdvice {
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> constraintViolationException() {
    return ResponseEntity.status(HttpStatus.CONFLICT).body("잘못된 입력 형식");
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> aaa() {
    return ResponseEntity.status(HttpStatus.CONFLICT).body("글을 읽을 수 없습니다");
  }
}
