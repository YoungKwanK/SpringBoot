package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.AuthorCreateDto;
import com.beyond.basic.b2_board.author.dto.AuthorDetailDto;
import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.author.dto.AuthorUpdatePwDto;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// 스프링에서 메서드 단위로 트랜잭션처리를 하고, 만약 예외(unchecked)발생 시 자동 롤백처리 지원.
@Transactional
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
    private final AuthorRepository authorRepository;

    // 객체 조립은 서비스 담당
    public void save(AuthorCreateDto authorCreateDto) {
//        이메일 중복 검증
        if (authorRepository.findByEmail(authorCreateDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 이메일이 존재합니다.");
        }
//        Author author = new Author(authorCreateDto.getName(), authorCreateDto.getEmail(), authorCreateDto.getPassword());
        Author author = authorCreateDto.authorToEntity();
        authorRepository.save(author);
    }

    // 트랜잭션이 필요 없는 경우, 아래와 같이 명시적으로 제외
    @Transactional(readOnly = true)
    public List<AuthorListDto> findAll(){
        return authorRepository.findAll()
                .stream()
                .map(a->a.authorListDtoFromEntity())
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public AuthorDetailDto findById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(()->new NoSuchElementException("해당 ID가 존재하지 않습니다."));
//        return new AuthorDetailDto(author.getId(), author.getName(), author.getEmail());
        AuthorDetailDto authorDetailDto = AuthorDetailDto.fromEntity(author);
        return authorDetailDto;
    }

    public void updatePassword(AuthorUpdatePwDto authorUpdatePwDto) {
        Author author = authorRepository.findByEmail(authorUpdatePwDto.getEmail())
                .orElseThrow(()->new NoSuchElementException());
//        dirty checking : 객체를 수정한 후 별도의 update쿼리 발생시키지 않아도, 영속성 컨텍스트에 의해 객체 변경사항 자동 DB반영
        author.updatePw(authorUpdatePwDto.getPassword());
    }

    public void delete(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(()->new NoSuchElementException("없는 사용자입니다."));
        authorRepository.delete(author);
    }
}
