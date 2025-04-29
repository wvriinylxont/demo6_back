package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class MemberService {
  @Autowired
  private MemberDao memberDao;

  public boolean checkUsername(MemberDto.UsernameCheck dto) {
    return !memberDao.existsByUsername(dto.getUsername());
  }

  public Member signup(MemberDto.Create dto) {
    // 비밀번호를 암호화했다고 치자
    String encodedPassword = dto.getPassword();
    Member member = dto.toEntity(encodedPassword);
    memberDao.save(member);
    return member;
  }

  public Optional<String> searchUseraname(String email) {
    return memberDao.findUsernameByEmail(email);
  }
}







