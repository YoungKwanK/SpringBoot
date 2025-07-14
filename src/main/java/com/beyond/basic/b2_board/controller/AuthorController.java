package com.beyond.basic.b2_board.controller;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController // Controller + ResponseBody
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;
    // 회원가입
    @PostMapping("/create")
    public String save(@RequestBody AuthorCreateDto authorCreateDto) {
        try{
            authorService.save(authorCreateDto);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            return e.getMessage();
        }
      return "ok";
    }
    
    // 회원목록조회 : /author/list
    @GetMapping("/list")
    public List<AuthorListDto> findByAll(){
        return authorService.findAll();
    }
    
    // 회원상세조회(id) : id로 조회. /author/detail/1
    // 서버에서 별도의 try catch하지 않으면, 에러 발생 시 500에러 + 스프링의 포맷으로 에러를 리턴.
    @GetMapping("/detail/{id}")
    public AuthorDetailDto findById (@PathVariable Long id)throws NoSuchElementException {
        try {
            return authorService.findById(id);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return null;
    }
    
    // 비밀번호수정 : email.password -> json. /author/updatepw
    // get:조회, post:등록, patch:부분수정, put:대체, update
    @PatchMapping("/updatepw")
    public void updatepw(@RequestBody AuthorUpdatePwDto authorUpdatePwDto) {
        authorService.updatePassword(authorUpdatePwDto);
    }


    // 회원탈퇴(삭제) : /author/de/1
    @DeleteMapping("/de/{id}")
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
}
