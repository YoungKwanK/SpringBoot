package com.beyond.basic.b2_board.controller;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.CommonDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entity")
public class ResponseEntityController {

//    case1. 일반적인 패턴
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/annotation1")
    public String annotation1() {
        return "ok";
    }

//    case2. 메서드 체이닝 방식
    @GetMapping("/channing")
    public ResponseEntity<?> channing1() {
        Author author = new Author("test", "test", "test");
        return ResponseEntity.status(HttpStatus.CREATED).body(author);
    }
    
//    case3. ResponseEntity 객체를 직접 생성하는 방식(가장 많이 사용)
    @GetMapping("/custom1")
    public ResponseEntity<?> custom1() {
        Author author = new Author("test", "test", "test");
        return new ResponseEntity<>(author,HttpStatus.CREATED);
    }

    @GetMapping("/custom2")
    public ResponseEntity<?> custom2() {
        Author author = new Author("test", "test", "test");
        return new ResponseEntity<>(new CommonDto(author, HttpStatus.CREATED.value(), "author is created"),HttpStatus.CREATED);
    }
}
