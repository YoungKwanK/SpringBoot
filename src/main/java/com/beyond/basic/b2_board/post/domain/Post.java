package com.beyond.basic.b2_board.post.domain;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(length = 3000)
    private String contents;

    @JoinColumn(name = "author_id") // fk관계성
    //  FK설정 시 ManyToOne필수
    // ManyToOne에서는 default fetch.EAGER(즉시로딩) : author 객체를 사용하지 않아도 author테이블로 쿼리 발생
    // 그래서, 일반적으로 LAZY(지연로딩) 설정 : author 객체를 사용하지 않는 한 author객체로 쿼리발생X
    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;
}
