package com.sparta.homeworkjwt.controller;

import com.sparta.homeworkjwt.dto.PostRequestDto;
import com.sparta.homeworkjwt.dto.PostResponseDto;
import com.sparta.homeworkjwt.dto.ResponseDto;
import com.sparta.homeworkjwt.entity.Post;
import com.sparta.homeworkjwt.service.PostService;
import lombok.RequiredArgsConstructor;
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
    public ResponseDto<PostResponseDto> createPost(PostRequestDto postRequestDto, HttpServletRequest request) {
        return postService.createPost(postRequestDto, request);
    }

    @GetMapping("/post/{id}")
    public ResponseDto<PostResponseDto> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @PutMapping("/post/{id}")
    public ResponseDto<PostResponseDto> updatePost(@PathVariable Long id, PostRequestDto postRequestDto, HttpServletRequest request) {
        return postService.updatePost(id, postRequestDto, request);
    }

    @DeleteMapping("/post/{id}")
    public ResponseDto<String> updatePost(@PathVariable Long id, HttpServletRequest request) {
        return postService.deletePost(id, request);
    }

}
