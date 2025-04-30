package com.example.demo.dao;

import com.example.demo.entity.*;
import org.apache.ibatis.annotations.*;

import java.util.*;

@Mapper
public interface PostDao {
  // SelectKey의 실행결과를 save의 파라미터인 post에 저장한다 -> 서비스에서 post.getPno()로 시퀀스값에 접근가능
  @SelectKey(statement="select posts_seq.nextval from dual", before=true, keyProperty = "pno", resultType=Integer.class)
  @Insert("insert into posts values(#{pno}, #{title}, #{content}, #{writeTime},#{writer},#{readCnt},#{goodCnt},#{badCnt})")
  int save(Post post);

  @Select("select * from posts order by pno desc offset (#{pageno}-1) * #{pagesize} rows fetch next #{pagesize} rows only")
  List<Post> findAll(int pageno, int pagesize);
}
