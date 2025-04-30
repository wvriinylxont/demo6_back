package com.example.demo.util;

import org.springframework.web.multipart.*;

import java.io.*;
import java.util.*;

public class Demo6Util {
  // try ~ catch는 예외 처리 작업
  // throws는 예외를 처리하지 않고, 일시킨 사람에게 떠넘긴다
  // 지금은 서비스에서 이미지를 base64로 바꿔라...그러면 문제가 발생하면 대처도 서비스가 해야지
  public static String convertToBase64(MultipartFile file) throws IOException {
    byte[] fileBytes =  file.getBytes();
    // contentType는 파일의 형식. 예) image/jpg, image/png
    // base64 형식으로 데이터를 브라우저에 출력할 때
    //    웹브라우저가 데이터의 종류를 모르면 저장 메뉴를 띄운다
    //    데이터 앞에 파일 형식을 지정하면, 웹브라우저가 처리
    return "data:" + file.getContentType() + ";base64," + Base64.getEncoder().encodeToString(fileBytes);
  }
}
