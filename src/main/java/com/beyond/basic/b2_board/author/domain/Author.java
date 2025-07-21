package com.beyond.basic.b2_board.author.domain;

import com.beyond.basic.b2_board.author.dto.AuthorListDto;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@ToString
// JPA를 사용할 경우 Entity 반드시 붙여야하는 어노테이션
// JPA의 Entity Manager에게 객체를 위임하기 위한 어노테이션
// 엔티티 매니저는 영속성컨텍스트를 통해 db(현재 상황) 데이터 관리
@Entity
// Builder 어노테이션을 통해 유연하게 객체 생성 가능.
@Builder
public class Author {
    @Id // pk설정
//    IDENTITY : auto_increment
//    AUTO : id 생성 전략을 jpa에게 자동 설정하도록 위임하는 것.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    컬럼에 별다른 설정이 없을 경우 default varchar(255)
    private String name;
    @Column(length = 50, unique = true, nullable = false)
    private String email;
//    @Column(name = "password") : 되도록이면 컬럼명과 변수명을 일치시키는 것이 개발의 혼선을 줄일 수 있음
    private String password;
    @Enumerated(EnumType.STRING)
    @Builder.Default //  빌더패턴에서 변수 초기화(디폴트값) 시 Builder.Default어노테이션 필수
    private Role role = Role.USER;
//    컬럼명에 캐멀케이스 사용시, db에는 created_time으로 컬럼 생성
    @CreationTimestamp
    private LocalDateTime createdTime;
    @UpdateTimestamp
    private LocalDateTime updatedTime;

    // @OneToMany는 선택 사항, @ManyToOne과 달리 fetch 옵션의 default가 FetchType.LAZY
    // mappedBy 에는 ManyToOne 쪽에 변수명을 문자열로 지정
    // mappedBy를 지정해야 하는 이유는 FK 관리를 매핑되어 있는 (Post) 쪽에서 한다는 의미 => 연관 관계의 주인 설정
    // cascade : 부모 객체의 변화에 따라 자식 객체가 같이 변하는 옵션 1)persist : 저장 2)remove : 삭제
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<Post> postList = new ArrayList<>();            // @OneToMany 설정 시 List 초기화 필수, @Builder.Default 설정 필수

    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    public void updatePw(String password) {
        this.password = password;
    }

//    public AuthorDetailDto authorDetailDtoFromEntity() {
//        return new AuthorDetailDto(this.id, this.name, this.email);
//    }

    public AuthorListDto authorListDtoFromEntity() {
        return new AuthorListDto(this.id, this.name, this.email);
    }
}
