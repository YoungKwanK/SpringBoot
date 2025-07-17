package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.author.service.AuthorService;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateDto dto){
//        authorId가 실제 있는 지 없는 지 검증.
        Author author = authorRepository.findById(dto.getAuthorId()).orElseThrow(()->new EntityNotFoundException("없는 사용자입니다."));
        postRepository.save(dto.toEntity(author));
    }

    @Transactional(readOnly = true)
    public PostDetailDto findById(Long id){
        Post post = postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("없는 ID입니다."));
//        엔티티 간의 관계성 설정을 하지 않았을 때
//        Author author = authorRepository.findById(post.getAuthorId()).orElseThrow(()->new EntityNotFoundException("없는 회원입니다."));
//        return PostDetailDto.fromEntity(post, author);
        
//        엔티티 간의 관계성 설정을 통해 Author 객체를 쉽게 조회하는 경우
        return PostDetailDto.fromEntity(post);
    }

    @Transactional(readOnly = true)
    public List<PostListDto> findAll(){
        return postRepository.findAll()
                .stream()
                .map(a->PostListDto.postListDtoFromEntity(a))
                .toList();
    }
}
