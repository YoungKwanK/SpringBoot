package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.dto.PostSearchDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
//@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateDto postCreateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // claims의 subject : email
        // authorId가 실제 있는 지 없는 지 검증 필요
//        Author author = authorRepository.findById(postCreateDTO.getAuthorId())
//                .orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
//        postRepository.save(postCreateDTO.toEntity(author));
        Author author = authorRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
        LocalDateTime appointmentTime = null;
        if(postCreateDto.getAppointment().equals("Y")){
            if (postCreateDto.getAppointmentTime()==null || postCreateDto.getAppointmentTime().isEmpty()){
                throw new IllegalArgumentException("시간 정보가 비어져 있습니다");
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            appointmentTime = LocalDateTime.parse(postCreateDto.getAppointmentTime(), dateTimeFormatter);
        }
        postRepository.save(postCreateDto.toEntity(author, appointmentTime));
    }

    public PostDetailDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("없는 Id 입니다."));

        // 엔티티간 관계성 설정하지 않았을 경우
//        Author author = authorRepository.findById(post.getAuthorId())
//                .orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));
//
//        return PostDetailDTO.fromEntity(post, author);

        // 엔티티 간 관계성 설정을 통해 Author 객체를 쉽게 조회하는 경우
        return PostDetailDto.fromEntity(post);

    }

    public List<PostListDto> findAll() {
//        List<Post> postList = postRepository.findAll();                 // 일반 전체 조회 -> N + 1 문제 발생
//        List<Post> postList = postRepository.findAllJoin();             // 일반 inner join -> N + 1 문제 발생
//        List<Post> postList = postRepository.findAllFetchJoin();        // inner join fetch -> 1개의 쿼리만 발생
        // postList 조회할 때 참조 관계에 있는 author 까지 조회하게 되기 때문에 N + 1 문제 발생 (N : author 쿼리, 1 : post 쿼리)
        // JPA는 기본 방향성이 fetch lazy 이므로 참조하는 시점에 쿼리를 내보내게 되어 JOIN문을 만들어 주지 않고, N + 1 문제 발생
        // 이후에는 페이징 처리할 것이라 findAll() 안함


        // 페이징 처리 findAll()
        List<Post> postList = postRepository.findAll();
        return postList.stream().map(a -> PostListDto.postListDtoFromEntity(a)).collect(Collectors.toList());
    }

    public Page<PostListDto> findAll(Pageable pageable, PostSearchDto postSearchDto) {
        // 검색을 위해 Specification 객체 스프링에서 제공
        // 페이징 처리 findAll()
//        Specification객체는 복잡한 쿼리를 명세를 이용하여 정의하는 방식으로, 쿼리를 쉽게 생성.
//        Page<Post> postList = postRepository.findAll(pageable);
        Specification<Post> specification = new Specification<Post>() {
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                Root : 엔티티와 속성을 접근하기 위한 객체, CriteriaBuilder : 쿼리를 생성하기 위한 객체
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(cb.equal(root.get("delYn"), "N"));
                predicateList.add(cb.equal(root.get("appointment"), "N"));
                if(postSearchDto.getCategory()!=null){
                    predicateList.add(cb.equal(root.get("category"), postSearchDto.getCategory()));
                }
                if(postSearchDto.getTitle()!=null){
                    predicateList.add(cb.like(root.get("title"), "%"+postSearchDto.getTitle()+"%"));
                }
                Predicate[] predicateArr = new Predicate[predicateList.size()];
                predicateList.toArray(predicateArr);

//                위의 검색 조건들을 하나(한줄)의 Predicate객체로 만들어서 return
                Predicate predicate = cb.and(predicateArr);
                return predicate;
            }
        };
        Page<Post> postList = postRepository.findAll(specification ,pageable);
        return postList.map(a -> PostListDto.postListDtoFromEntity(a));
    }
}