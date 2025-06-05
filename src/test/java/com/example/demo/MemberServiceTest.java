package com.example.demo;

import com.example.demo.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberServiceTest {
    @Autowired
    private MemberService service;

    @Test
    public void mail() {
        String link = "<a href='#'>링크</a>";
        service.sendMail("c##spring@icia.com", "sera7418@naver.com", "제목입니다", link);
    }
}
