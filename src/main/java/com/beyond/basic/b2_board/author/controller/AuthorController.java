package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.author.dto.AuthorLoginDto;
import com.beyond.basic.b2_board.author.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.common.JwtTokenProvider;
import com.beyond.basic.b2_board.common.dto.CommonDto;
import com.beyond.basic.b2_board.author.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController // Controller + ResponseBody
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthorRepository authorRepository;

    // 회원가입
    @PostMapping("/create")
//    dto에 있는 validation어노테이션과 controller @Valid가 한 쌍
    /* 아래 코드 포스트맨 테스트 데이터 예시
        1. multipart-formdata 선택
        2. authorCreateDto를 text로
        {
            "name":"test1",
            "email" : "test2@naver.com",
            "password":"1321425123"
        }
        세팅하면서 content-type를 application/json 설정
        3. profileImage는 file로 세팅하면서 content-type를 multipart/form-data로 설정
     */
//    파일 안넣으면 오류 나오기 때문에 required = false(default가 true임)
    public ResponseEntity<?> save(@RequestPart(name = "authorCreateDto") @Valid AuthorCreateDto authorCreateDto,
                                  @RequestPart(name="profileImage", required = false) MultipartFile profileImage) {
        System.out.println(profileImage.getOriginalFilename());
//        try{
//            authorService.save(authorCreateDto);
//            return new ResponseEntity<>("OK", HttpStatus.CREATED);
//        }catch (IllegalArgumentException e){
//            e.printStackTrace();
////            생성자 매개변수 body부분의 객체와 header부에 상태코드
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
        // controllerAdvice가 없었으면 위와 같이 개별적인 예외처리가 필요하나, 이제는 전역적인 예외처리가 가능.
        authorService.save(authorCreateDto, profileImage);
        return new ResponseEntity<>(new CommonDto("",HttpStatus.CREATED.value(), "성공 ~"), HttpStatus.CREATED);
    }
    
    // 회원목록조회 : /author/list
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuthorListDto> findByAll(){
        return authorService.findAll();
    }
    
    // 회원상세조회(id) : id로 조회. /author/detail/1
    // 서버에서 별도의 try catch하지 않으면, 에러 발생 시 500에러 + 스프링의 포맷으로 에러를 리턴.
    @GetMapping("/detail/{id}")
//    ADMIN권한이 있는 지를 AUTHENTICATION 객체에서 쉽게 확인(ROLE_를 떼어줌)
//    권한이 없을 경우 filterchain에서 에러 발생
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findById (@PathVariable Long id)throws NoSuchElementException {
//            return new ResponseEntity<>(authorService.findById(id), HttpStatus.OK);
        return new ResponseEntity<>(new CommonDto(authorService.findById(id),HttpStatus.OK.value(), "성공 ~"), HttpStatus.OK);
    }

    @GetMapping("/myinfo")
    public ResponseEntity<?> findMyInfo()throws NoSuchElementException {
        return new ResponseEntity<>(new CommonDto(authorService.myinfo(),HttpStatus.OK.value(), "성공 ~"), HttpStatus.OK);
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
    
    // 로그인
    @PostMapping("doLogin")
    public ResponseEntity<?> login(@RequestBody AuthorLoginDto authorLoginDto){
        Author author = authorService.login(authorLoginDto);
//        토큰 생성 및 return
        String token = jwtTokenProvider.createAtToken(author);
        return new ResponseEntity<>(new CommonDto(token, HttpStatus.OK.value(), "token is created")
                ,HttpStatus.OK);
    }
}
