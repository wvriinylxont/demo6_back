package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.*;

public class MemberDto {
  @Data
  public static class Create {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String email;
    private MultipartFile profile;
  }

  public static class Update {

  }

  public static class Read {

  }
}
