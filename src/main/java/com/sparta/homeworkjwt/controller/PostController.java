package com.sparta.homeworkjwt.controller;

import com.sparta.homeworkjwt.dto.PostRequestDto;
import com.sparta.homeworkjwt.dto.PostResponseDto;
import com.sparta.homeworkjwt.dto.ResponseDto;
import com.sparta.homeworkjwt.entity.Post;
import com.sparta.homeworkjwt.security.UserDetailsImpl;
import com.sparta.homeworkjwt.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ResponseDto<List<PostResponseDto>> posts() {

        return postService.getPosts();
    }

    @PostMapping("/post")
    public ResponseDto<PostResponseDto> createPost(PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(postRequestDto, userDetails);
    }

    @GetMapping("/post/{id}")
    public ResponseDto<PostResponseDto> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PutMapping("/post/{id}")
    public ResponseDto<PostResponseDto> updatePost(@PathVariable Long id, PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, postRequestDto, userDetails);
    }

    @DeleteMapping("/post/{id}")
    public ResponseDto<String> updatePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails);
    }

    @PostMapping("/post/like/{id}")
    public ResponseDto<String> likePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.likePost(id, userDetails);
    }

}
