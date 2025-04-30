package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.util.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

import java.io.*;
import java.util.*;

@Service
public class MemberService {
  @Autowired
  private MemberDao memberDao;

  public boolean checkUsername(MemberDto.UsernameCheck dto) {
    return !memberDao.existsByUsername(dto.getUsername());
  }

  public Member signup(MemberDto.Create dto) {
    // 1. 비밀번호 암호화
    String encodedPassword = dto.getPassword();
    // 2. 프사를 업로드했다면 저장을 위해 base64 인코딩
    MultipartFile profile = dto.getProfile();
    String base64Image = "";
    if(!profile.isEmpty()) {
      try {
        base64Image = Demo6Util.convertToBase64(profile);
      } catch(IOException e) {
        e.printStackTrace();
      }
    }
    // 3. 암호화된 비밀번호, base64이미지를 가지고 dto를 member로 변환
    Member member = dto.toEntity(encodedPassword, base64Image);
    memberDao.save(member);
    return member;
  }

  public Optional<String> searchUseraname(String email) {
    return memberDao.findUsernameByEmail(email);
  }

  public Optional<String> getTemporaryPassword(MemberDto.GeneratePassword dto) {
    // 1. 아이디와 이메일이 일치하는 사용자가 있는 지 확인
    // 2. 사용자가 없을 경우 비어있는 Optional을 리턴 -> 컨트롤러에서 if문으로 처리
    // 3. 있을 경우 임시비밀번호를 생성
    // 4. 임시비밀번호를 암호화해서 업데이트
    // 5. 비밀번호를 Optional로 리턴
    boolean isExist = memberDao.existsByUsernameAndEmail(dto);
    if(!isExist)
      return Optional.empty();
    String newPassword = RandomStringUtils.secure().nextAlphanumeric(20);
    memberDao.updatePassword(dto.getUsername(), newPassword);
    return Optional.ofNullable(newPassword);
  }
}







