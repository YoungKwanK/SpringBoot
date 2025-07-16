package com.beyond.basic.b2_board.repository;

import com.beyond.basic.b2_board.domain.Author;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
