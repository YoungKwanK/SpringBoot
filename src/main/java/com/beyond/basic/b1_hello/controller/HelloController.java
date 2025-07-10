package com.beyond.basic.b1_hello.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// Component어노테이션을 통해 별도의 객체를 생성할 필요가 없는, 싱글톤 객체 생성
// Controller 어노테이션을 통해 쉽게 사용자의 http req를 분석하고, http res를 생성
@Controller
// 클래스차원에 url 매핑시에는 RequestMapping을 사용
@RequestMapping("/hello")
public class HelloController {
    //    get 요청의 case들
//    case1. 서버가 사용자에게 단순 String 데이터 return - @ResponseBody있을 때
    @GetMapping("") // 아래 메서드에 대한 서버의 엔드포인트를 설정
//    ResponseBody가 없고, return타입이 String인 경우 서버는 templates폴더 밑에 helloworld.html을 찾아서 리턴
    @ResponseBody
    public String helloWorld() {
        return "helloworld";
    }

    //  case2. 서버가 사용자에게 String(json형식)의 데이터 return
    @GetMapping("/json")
    @ResponseBody
    public Hello helloJson() throws JsonProcessingException {
        Hello h1 = new Hello("hong", "hong@naver.com");
//        ObjectMapper mapper = new ObjectMapper();
//        String result = mapper.writeValueAsString(h1);
        return h1;
    }

    //    case3. parameter방식을 통해 사용자로부터 값을 수신
//    parameter의 형식 : /member?name=hogildong
    @GetMapping("/param")
    @ResponseBody
    public Hello param(@RequestParam(value = "name") String inputName) {
        System.out.println(inputName);
//        {name:사용자가 넣어온 이름, email:아무거나}
        Hello h1 = new Hello(inputName, "hong@naver.com");
        return h1;
    }

    //    case4. pathvariable방식을 통해 사용자로부터 값을 수신
//    pathvariable의 형식 : /member/1
//    pathvariable방식은 url을 통해 자원의 구조를 통해 자원의 구조를 명확하게 표현할 때 사용(좀 더 restful함)
    @GetMapping("/path/{inputId}")
    @ResponseBody
    public String path(@PathVariable Long inputId) {
//        별도의 형변환 없이도, 매개변수에 타입 지정 시 자동형변환 시켜줌.
//        long id = Long.parseLong(inputId);
        System.out.println(inputId);
        return "ok";
    }

    //    case5. parameter 2개 이상 형식
//    /hello/param2?name=hong&email=hong@naver.com
    @GetMapping("/param2")
    @ResponseBody
    public String param2(@RequestParam(value = "name") String inputName, @RequestParam(value = "email") String inputEmail) {
        System.out.println("name : " + inputName + "email : " + inputEmail);
        return "ok";
    }
}
