package com.example.demo.dao;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import jakarta.validation.constraints.*;
import org.apache.ibatis.annotations.*;

import java.util.*;

@Mapper
public interface MemberDao {
  @Select("select count(*) from members where username=#{username}")
  boolean existsByUsername(String username);

  int save(Member member);

  @Select("select username from members where email=#{email} and rownum=1")
  Optional<String> findUsernameByEmail(String email);

  @Select("select count(*) from members where username=#{dto.username} and email=#{dto.email} and rownum=1")
  boolean existsByUsernameAndEmail(@Param("dto") MemberDto.GeneratePassword dto);

  @Update("update members set password=#{newPassword} where username=#{username}")
  void updatePassword(String username, String newPassword);

  @Select("select username, password, role, is_lock from members where username=#{username}")
  Optional<Member> loadLoginData(String username);
}








