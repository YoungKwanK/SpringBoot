package com.beyond.basic.b2_board.service;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.dto.AuthorListDto;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service // Component로도 대체 가능(트랜잭션처리가 없는 경우)
public class AuthorService {
//    의존성 주입(DI) 방법 1. Autowired 어노테이션 사용. -> 필드주입
//    @Autowired
//    private AuthorRepository authorRepository;

//    의존성주입(DI)방법2. 생성자주입방식(가장 많이 쓰는 방식)
//    장점1) final을 통해 상수로 사용 가능(안전성 향상) 장점2) 다형성 구현 가능 장점3) 순환참조방식(컴파일타임에 check)
//    private final AuthorRepositoryInterface authorRepository;
//    객체로 만들어지는 시점에 스프링에서 authorRepository 객체를 주입
//    @Autowired 생성자가 하나밖에 없을 때에는 Autowired생략 가능
//    public AuthorService(AuthorMemoryRepository authorRepository) {
//        this.authorRepository = authorRepository;
//    }

    //    의존성주입방법3. RequiredArgs 어노테이션 사용 : 반드시 초기화 되어야 하는 필드(final)을 대상으로 생성자를 자동 생성
    //    다형성 설계는 불가
    private final AuthorMemoryRepository authorRepository;

    // 객체 조립은 서비스 담당
    public void save(AuthorCreateDto authorCreateDto) {
//        이메일 중복 검증
        if (authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 이메일이 존재합니다.");
        }
        Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
        this.authorRepository.save(author);
    }

    public List<AuthorListDto> findAll(){
        List<AuthorListDto> authorListDto = new ArrayList<>();
        for (Author author : authorRepository.findAll()){
            authorListDto.add(new AuthorListDto(author.getId(), author.getEmail(), author.getEmail()));
        }
        return authorListDto;
    }

    public AuthorDetailDto findById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(()->new NoSuchElementException("해당 ID가 존재하지 않습니다."));
        AuthorDetailDto authorDetailDto = new AuthorDetailDto(author.getId(), author.getEmail(), author.getEmail());
        return authorDetailDto;
    }

    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {
        Author author = authorRepository.findByEmail(authorUpdatePwDto.getEmail()).orElseThrow(()->new NoSuchElementException());
        author.updatePw(author.getPassword());
    }

    public void delete(Long id) {
        authorRepository.findById(id).orElseThrow(()->new NoSuchElementException("없는 사용자입니다."));
        authorRepository.delete(id);
    }
}
