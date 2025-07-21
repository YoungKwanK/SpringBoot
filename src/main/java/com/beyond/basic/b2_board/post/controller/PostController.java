package com.beyond.basic.b2_board.post.controller;

import com.beyond.basic.b2_board.author.dto.CommonDto;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody PostCreateDto postCreateDTO) {
        postService.save(postCreateDTO);
        return new ResponseEntity<>(new CommonDto("OK", HttpStatus.CREATED.value()
                , "post is created"), HttpStatus.CREATED);
    }

    // 게시글 목록 조회
    @GetMapping("/list")
//    public ResponseEntity<?> findAll() {
    public ResponseEntity<?> findAll() {
        List<PostListDto> postListDTO = postService.findAll();

        return new ResponseEntity<>(new CommonDto(postListDTO, HttpStatus.OK.value(), "OK"), HttpStatus.OK);
    }

    // 게시글 목록 조회 - 페이징 처리
    // 페이징 처리를 위한 데이터 요청 형식 : post/list?page=0&size=20&sort=title,desc
    @GetMapping("/listPaging")
    public ResponseEntity<?> findAll(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                     Pageable pageable) {
        Page<PostListDto> postListDTO = postService.findAll(pageable);
        return new ResponseEntity<>(new CommonDto(postListDTO, HttpStatus.OK.value(), "OK"), HttpStatus.OK);
    }

    // 게시글 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        PostDetailDto postDetailDTO = postService.findById(id);
        return new ResponseEntity<>(new CommonDto(postDetailDTO, HttpStatus.OK.value()
                , "post is found"), HttpStatus.OK);
    }
}