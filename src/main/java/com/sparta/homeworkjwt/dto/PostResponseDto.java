package com.sparta.homeworkjwt.dto;

import com.sparta.homeworkjwt.entity.Comment;
import com.sparta.homeworkjwt.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String username;
    private String content;
    private LocalDateTime regDt;
//    private List<Comment> comments;
    private List<CommentResponseDto> comments = new ArrayList<>();

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUser().getUsername();
        this.content = post.getContent();
        this.regDt = post.getCreatedAt();
//        this.comments = post.getComments();
        for(Comment comment : post.getComments()) {
//            this.comments.add(new CommentResponseDto(comment.getId(), comment.getContent(), comment.getUser().getUsername(), comment.getCreatedAt(), comment.getModifiedAt()));
            this.comments.add(CommentResponseDto.from(comment));
        }
    }

}
