package com.example.demo.execise;

import org.apache.ibatis.annotations.*;

import java.util.*;

@Mapper
public interface EmpDao {
  // emp에서 job을 출력하는 쿼리와 메소드
  @Select("select distinct job from emp order by job asc")
  List<String> findJob1();

  // 부서번호를 입력받아 그 부서의 사원 출력
  @Select("select * from emp where deptno=#{deptno}")
  List<Map<String,Object>> findEmpByDeptno1(int deptno);

  // comm을 수령하는 사원들 출력
  @Select("select * from emp where comm is not null")
  List<Map<String,Object>> findEmpByCommIsNotNull1();

  List<String> findJob2();

  // 일정 급여 이하를 출력
  // select * from emp where sal<=?
  List<Map<String,Object>> findEmpBySalLessThan2(int sal);

  List<Map<String, Object>> findEmpByDname(String dname);

  // 사번으로 검색
  Optional<Map<String, Object>> findByEmpno(int empno);
}
