package com.beyond.basic.b2_board.post.domain;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 3000)
    private String contents;

    // 게시글 삭제 여부 (게시글 목록 조회 시 "Y"는 제외)
    @Builder.Default
    private String delYn="N";

    // FK 설정 시 ManyToOne 필수
    // ManyToOne 에서는 default FetchType.EAGER (즉시 로딩) : 참조 테이블 바로 조회; author 객체를 사용하지 않아도 author 테이블로 쿼리 발생
    // 그래서 일반적으로 FetchType.LAZY (지연 로딩) 설정 : 테이블 참조 시 조회; author 객체를 사용하지 않는 한, author 테이블 쿼리 발생X
    // JPA 는 전체적으로 FetchType.LAZY 를 지향함
    @ManyToOne(fetch = FetchType.LAZY)      // post 입장에서 n:1 관계
    @JoinColumn(name = "author_id")         // FK 관계성 설정 어노테이션
    private Author author;

    @Builder.Default
    private String appointment="N";
    private LocalDateTime appointmentTime;

    public void updateAppointment(String appointment) {
        this.appointment = appointment;
    }
}