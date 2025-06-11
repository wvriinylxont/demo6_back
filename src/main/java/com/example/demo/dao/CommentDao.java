package com.example.demo.dao;

import com.example.demo.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.*;

@Mapper
public interface CommentDao {
  @Insert("insert into comments(cno, content,write_time, writer, pno) values(comments_seq.nextval, #{content}, sysdate, #{writer}, #{pno})")
  int save(int pno, String content, String writer);

  @Select("select * from comments where pno=#{pno} order by cno desc")
  List<Comment> findByPno(int pno);

  @Delete("delete from comments where cno=#{cno} and writer=#{loginId}")
  int deleteByCnoAndWriter(int cno, String loginId);
}
