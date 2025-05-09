package com.example.demo;

import com.example.demo.execise.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class EmpDaoTest {
  @Autowired
  private EmpDao empDao;

  //@Test
  public void findJob1Test() {
    empDao.findJob1().forEach(a->System.out.println(a));
    assertEquals(1, empDao.findJob1().size() );
  }

  //@Test
  public void findEmpByDeptno1Test() {
    //empDao.findEmpByDeptno1(10).forEach(a->System.out.println(a));
    assertEquals(3, empDao.findEmpByDeptno1(10).size());
  }

  //@Test
  public void findEmpByCommIsNotNull1Test() {
    assertEquals(4, empDao.findEmpByCommIsNotNull1().size());
  }

 // @Test
  public void findJob2Test() {
    assertEquals(5, empDao.findJob2().size() );
  }

  @Test
  public void findEmpBySalLessThan2Test() {
    empDao.findEmpBySalLessThan2(5000).forEach(a->System.out.println(a.get("JOB")));
    // Entity로 리턴을 받았다면 a.getJob();  장점: 오타는 절대 발생 X, 단점: 클래스를 만들어야 한다
    // Map으로 리턴을 받으면 a.get("JOB");   장점: 클래스를 만들 필요 X(간편). 단점 : 오타나면 니 책임
    assertEquals(14, empDao.findEmpBySalLessThan2(5000).size());
  }

  @Test
  public void findEmpByDnameTest() {
    assertEquals(6, empDao.findEmpByDname("SALES").size());
  }

  @Test
  public void findByEmpnoTest() {
    assertEquals(true, empDao.findByEmpno(7369).isPresent());
    assertEquals(false, empDao.findByEmpno(9000).isPresent());
  }
}
