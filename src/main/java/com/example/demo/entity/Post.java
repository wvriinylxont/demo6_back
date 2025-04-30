package com.example.demo.entity;

import lombok.*;

import java.time.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
  private int pno;
  private String title;
  private String content;
  private String writer;
  @Builder.Default
  private LocalDateTime writeTime = LocalDateTime.now();
  @Builder.Default
  private int readCnt = 0;
  @Builder.Default
  private int goodCnt = 0;
  @Builder.Default
  private int badCnt = 0;
}
