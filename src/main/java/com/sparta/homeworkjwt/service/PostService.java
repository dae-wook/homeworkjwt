package com.sparta.homeworkjwt.service;


import com.sparta.homeworkjwt.component.JwtUtil;
import com.sparta.homeworkjwt.dto.PostRequestDto;
import com.sparta.homeworkjwt.dto.PostResponseDto;
import com.sparta.homeworkjwt.dto.ResponseDto;
import com.sparta.homeworkjwt.entity.Post;
import com.sparta.homeworkjwt.entity.User;
import com.sparta.homeworkjwt.entity.UserRoleEnum;
import com.sparta.homeworkjwt.repository.CommentRepository;
import com.sparta.homeworkjwt.repository.PostRepository;
import com.sparta.homeworkjwt.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    private final CommentRepository commentRepository;

    public ResponseDto<List<PostResponseDto>> getPosts() {
        List<Post> list = postRepository.findAll();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for(Post post : list) {
            postResponseDtoList.add(new PostResponseDto(post));
        }
        return ResponseDto.success(postResponseDtoList);
    }

    public ResponseDto<PostResponseDto> createPost(PostRequestDto postRequestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if(token != null) {
            if(jwtUtil.validateToken(token)) {
                claims = jwtUtil.getUserInfoFromToken(token);
                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow( () -> new NullPointerException("유저 정보를 찾을 수 없음"));
                Post post = new Post(postRequestDto, user);
                postRepository.save(post);

                return ResponseDto.success(new PostResponseDto(post));
            }

        }
        throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }


    public ResponseDto<PostResponseDto> getPost(Long id) {
        PostResponseDto postResponseDto = new PostResponseDto(postRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("존재하지 않는 게시글")));
        return ResponseDto.success(postResponseDto);
    }

    @Transactional
    public ResponseDto<PostResponseDto> updatePost(Long id, PostRequestDto postRequestDto, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        if(token != null) {
            if(jwtUtil.validateToken(token)) {
                Claims claims = jwtUtil.getUserInfoFromToken(token);
                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow( () -> new NullPointerException("유저 정보를 찾을 수 없음"));
                Post post = postRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("존재하지 않는 게시글"));
                if(user.getRole() == UserRoleEnum.ADMIN || user.getUsername().equals(post.getUser().getUsername())) {
                    // 게시글 작성자와 로그인 유저가 같거나 로그인 유저가 어드민일 때
                    post.update(postRequestDto);
                    return ResponseDto.success(new PostResponseDto(post));
                } else {
                    throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
                }
            }
        }
        throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }

    public ResponseDto<String> deletePost(Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if(token != null) {
            if(jwtUtil.validateToken(token)) {
                Claims claims = jwtUtil.getUserInfoFromToken(token);
                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow( () -> new NullPointerException("유저 정보를 찾을 수 없음"));
                Post post = postRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("존재하지 않는 게시글"));
                if(user.getRole() == UserRoleEnum.ADMIN || user.getUsername().equals(post.getUser().getUsername())) {
                    // 게시글 작성자와 로그인 유저가 같거나 어드민일때
                    postRepository.deleteById(id);
                }else {
                    throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
                }
                return ResponseDto.success("success");
            }
        }
        throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }
}
