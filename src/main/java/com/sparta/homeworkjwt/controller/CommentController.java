package com.sparta.homeworkjwt.controller;

import com.sparta.homeworkjwt.dto.CommentRequestDto;
import com.sparta.homeworkjwt.dto.CommentResponseDto;
import com.sparta.homeworkjwt.dto.ResponseDto;
import com.sparta.homeworkjwt.security.UserDetailsImpl;
import com.sparta.homeworkjwt.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{id}")
    public ResponseDto<CommentResponseDto> createComment(@PathVariable Long id, String content, HttpServletRequest request) {
        return commentService.createComment(id, content, request);
    }

    @PutMapping("/comment/{id}")
    public ResponseDto<CommentResponseDto> updateComment(@PathVariable Long id, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        return commentService.updateComment(id, commentRequestDto, request);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseDto<String> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        return commentService.deleteComment(id, request);
    }

    @PostMapping("/comment/like/{id}")
    public ResponseDto<String> likeComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.likeComment(id, userDetails);
    }

}
