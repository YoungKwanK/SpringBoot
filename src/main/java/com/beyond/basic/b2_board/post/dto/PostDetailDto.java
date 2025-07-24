package com.beyond.basic.b2_board.post.dto;


import com.beyond.basic.b2_board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PostDetailDto {

    private Long id;
    private String title;
    private String contents;
    private String authorEmail;
    private String category;

    // 엔티티 간 관계성 설정을 하지 않았을 경우
//    public static PostDetailDTO fromEntity(Post post, Author author) {
//        return PostDetailDTO.builder()
//                .id(post.getId())
//                .title(post.getTitle())
//                .contents(post.getContents())
//                .authorEmail(author.getEmail())
//                .build();
//    }

    // 엔티티 간 관계성 설정한 경우
    public static PostDetailDto fromEntity(Post post) {
        return PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .authorEmail(post.getAuthor().getEmail())
                .category(post.getCategory())
                .build();
    }
}