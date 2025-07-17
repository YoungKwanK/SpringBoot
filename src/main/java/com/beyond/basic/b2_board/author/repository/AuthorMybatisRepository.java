package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// mybatis repository로 만들 때 필요한 어노테이션
@Mapper
public interface AuthorMybatisRepository {
    void save(Author author);

    List<Author> findAll();

    Optional<Author> findById(Long inputId);

    Optional<Author> findByEmail(String inputEmail);

    void delete(Long id);
}
