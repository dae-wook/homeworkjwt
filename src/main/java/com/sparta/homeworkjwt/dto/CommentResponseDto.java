package com.sparta.homeworkjwt.dto;

import com.sparta.homeworkjwt.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt  = comment.getModifiedAt();
    }

}
