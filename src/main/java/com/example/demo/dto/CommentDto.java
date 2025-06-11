package com.example.demo.dto;

import com.example.demo.entity.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.*;

// 객체를 못 만들게 PRIVATE
// private CommentDto() {} 이거랑 똑같음
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDto {
    @Data
    public static class Create {
        @NotNull
        private Integer pno;
        @NotEmpty
        private String content;

        public Comment toEntity(String loginId) {
            return new Comment(0, content, loginId, LocalDateTime.now(), pno);
        }
    }

    @Data
    public static class Delete {
        @NotNull
        private Integer cno;
        @NotNull
        private Integer pno;
    }
}