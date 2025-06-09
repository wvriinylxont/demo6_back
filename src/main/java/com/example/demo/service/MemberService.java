package com.example.demo.service;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.util.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.web.multipart.*;

import java.io.*;
import java.util.*;

@Service
public class MemberService {
  @Autowired
  private MemberDao memberDao;
  @Autowired
  private PasswordEncoder encoder;
  @Autowired
  private JavaMailSender mailSender;

  // 이메일 발송
  public void sendMail(String 보낸이, String 받는이, String 제목, String 내용) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
      helper.setFrom(보낸이);
      helper.setTo(받는이);
      helper.setSubject(제목);
      // 두번째 파라미터는 html 활성화 여부 <a hre='aaa'>링크</a>
      helper.setText(내용, true);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    mailSender.send(mimeMessage);
  }

  public boolean checkUsername(MemberDto.UsernameCheck dto) {
    return !memberDao.existsByUsername(dto.getUsername());
  }

  public Member signup(MemberDto.Create dto) {
    // 1. 비밀번호 암호화
    String encodedPassword = encoder.encode(dto.getPassword());
    // 2. 프사를 업로드했으면 인코딩, 업로드하지 않았으면 기본 프사를 저장
    MultipartFile profile = dto.getProfile();
    // 프론트에 <input type='file' name='profile'>이 없다면 profile이 null이 된다
    // 이 경우 profile.isEmpty()는 null pointer exception(NPE)
    boolean 프사_존재 = profile!=null && !profile.isEmpty();
    String base64Image = "";
    try {
      if(프사_존재) {
        base64Image = Demo6Util.convertToBase64(profile);
      } else {
        base64Image = Demo6Util.getDefaultBase64Profile();
      }
    } catch(IOException e) {
      base64Image = Demo6Util.getDefaultBase64Profile();
    }
    // 3. 암호화된 비밀번호, base64이미지, 랜덤한 체크코드 를 가지고 dto를 member로 변환
    String code = RandomStringUtils.secure().nextAlphanumeric(20);
    Member member = dto.toEntity(encodedPassword, base64Image, code);

    // 4. 이메일 발송
    String checkUrl = "http://localhost:8080/api/members/verify?code=" + code;
    String html = "<p>가입해주셔서 감사합니다</p>";
    html += "<p>아래의 링크를 클릭하시면 가입이 완료됩니다</p>";
    html += "<a href='" + checkUrl + "'>링크</a>";

    memberDao.save(member);
    sendMail("c##spring@icia.com", member.getEmail(), "가입확인메일", html);
    return member;
  }

  public boolean verify(String code) {
    return memberDao.verfiyCode(code)==1;
  }

  public Optional<String> searchUsername(String email) {
    return memberDao.findUsernameByEmail(email);
  }

  public boolean getTemporaryPassword(MemberDto.FindByPassword dto) {
    Member member = memberDao.findByUsername(dto.getUsername());
    if(member == null)
      return false;
    String newPassword = RandomStringUtils.secure().nextAlphanumeric(10);
    memberDao.updatePassword(dto.getUsername(), encoder.encode(newPassword));

    String html = "<p>아래 임시번호로 로그인하세요</p>";
    html +="<p>" + newPassword + "</p>";
    sendMail("c##spring@icia.com", member.getEmail(), "임시비밀번호", html);
    return true;
  }

  public boolean checkPassword(MemberDto.CheckPassword dto, String loginId) {
    String encodedPassword = memberDao.findPasswordByUsername(loginId);
    if(encodedPassword == null)
      return false;
    return encoder.matches(dto.getPassword(), encodedPassword);
  }

  public MemberDto.Read read(String loginId) {
    Member member = memberDao.findByUsername(loginId);
    return member.toRead();
  }

  public boolean changePassword(MemberDto.PasswordChange dto, String loginId) {
    // 기존 암호화된 비밀번호를 읽어와 비밀번호가 맞는 지 확인 -> 틀리면 false
    String encodedPassword = memberDao.findPasswordByUsername(loginId);
    System.out.println(dto.getCurrentPassword());
    System.out.println(dto.getNewPassword());
    System.out.println(encodedPassword);

    if(!encoder.matches(dto.getCurrentPassword(), encodedPassword))
      return false;
    // 비밀번호가 일치한 경우 새 비밀번호로 업데이트
    return memberDao.updatePassword(loginId, encoder.encode(dto.getNewPassword()))==1;
  }

  public void resign(String loginId) {
    memberDao.delete(loginId);
  }

  public boolean checkPassword(String password, String loginId) {
    String encodedPassword = memberDao.findPasswordByUsername(loginId);
    return (encoder.matches(password, encodedPassword));
  }

  public MemberDto.Read changeProfile(MultipartFile profile, String loginId) {
    String base64Image = "";
    try {
      base64Image = Demo6Util.convertToBase64(profile);
      memberDao.updateProfile(base64Image, loginId);
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return memberDao.findByUsername(loginId).toRead();
  }
}







