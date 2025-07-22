package com.beyond.basic.b2_board.author.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.domain.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data // dto계층은 데이터의 안정성이 엔티티만큼 중요하지는 않으므로, setter도 일반적으로 추가.
public class AuthorCreateDto {
    @NotEmpty(message = "이름은 필수 입력 항목입니다.")
    private String name;
    @NotEmpty(message = "이메일은 필수 입력 항목입니다.")
    private String email;
    @NotEmpty(message = "패스워드는 필수 입력 항목입니다.")
    @Size(min = 8, message = "패스워드의 길이가 너무 짧습니다.")
    private String password;
//    문자열로 값이 넘어오면 Role에 값으로 매핑
    private Role role = Role.USER;

    public Author authorToEntity(String encodedPassword){
//        빌더 패턴은 매개변수의 개수와 순서에 상관없이 객체생성 가능
        return Author.builder()
                .name(this.name)
                .password(encodedPassword)
                .email(this.email)
                .role(this.role)
                .build();
    }
}
