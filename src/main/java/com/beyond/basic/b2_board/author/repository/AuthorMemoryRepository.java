package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorMemoryRepository {
    private List<Author> authorList = new ArrayList<>();

    public static Long id = 1L;
    public void save(Author author) {
        this.authorList.add(author);
        id++;
    }

    public List<Author> findAll() {
        return this.authorList;
    }

    public Optional<Author> findById(Long id){
        Author author = null;
        for(Author temp : this.authorList){
            if(temp.getId().equals(id)){
                author = temp;
            }
        }
        return Optional.ofNullable(author);
    }

    public Optional<Author> findByEmail(String email){
        Author author = null;
        for(Author temp : this.authorList){
            if(temp.getEmail().equals(email)){
                author = temp;
            }
        }
        return Optional.ofNullable(author);
    }

    public void delete(Long id){
//        id값으로 요소의 index값을 찾아 삭제
        Author author=findById(id).orElseThrow();
        authorList.remove(authorList.indexOf(author));
    }
}
