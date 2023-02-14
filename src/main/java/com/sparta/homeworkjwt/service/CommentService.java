package com.sparta.homeworkjwt.service;


import com.google.gson.Gson;
import com.sparta.homeworkjwt.component.JwtUtil;
import com.sparta.homeworkjwt.dto.CommentRequestDto;
import com.sparta.homeworkjwt.dto.CommentResponseDto;
import com.sparta.homeworkjwt.dto.ResponseDto;
import com.sparta.homeworkjwt.entity.Comment;
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

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final Gson gson;
    public ResponseDto<CommentResponseDto> createComment(Long id, String content, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if(token != null) {
            if(jwtUtil.validateToken(token)) {
                claims =
                        jwtUtil.getUserInfoFromToken(token);
                Post post = postRepository.findById(id).orElseThrow( () -> new NullPointerException("등록되지 않은 게시글ID"));
                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(() -> new NullPointerException("유저 정보를 찾을 수 없음"));
                Comment comment = new Comment(user, post, content);
                commentRepository.save(comment);
                return ResponseDto.success(new CommentResponseDto(comment));
            }
        }
        throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }

    @Transactional
    public ResponseDto<CommentResponseDto> updateComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        if(token != null) {
            if(jwtUtil.validateToken(token)) {
                Claims claims = jwtUtil.getUserInfoFromToken(token);
                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow( () -> new NullPointerException("유저 정보를 찾을 수 없음"));
                Comment comment = commentRepository.findById(id).orElseThrow(() -> new NullPointerException("댓글을 찾을 수 없음"));

                if(user.getRole() == UserRoleEnum.ADMIN || user.getUsername().equals(comment.getUser().getUsername())) {
                    //댓글 단 유저와 로그인 유저가 같거나 권한이 ADMIN 일 때
                    comment.update(commentRequestDto);
                    return ResponseDto.success(new CommentResponseDto(comment));
                }else {
                    throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
                }
            }
        }
        throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }

    public ResponseDto<String> deleteComment(Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if(token != null) {
            if(jwtUtil.validateToken(token)) {
                Claims claims = jwtUtil.getUserInfoFromToken(token);
                User user = userRepository.findByUsername(claims.getSubject()).orElseThrow( () -> new NullPointerException("유저 정보를 찾을 수 없음"));
                Comment comment = commentRepository.findById(id).orElseThrow(() -> new NullPointerException("댓글을 찾을 수 없음"));

                if(user.getRole() == UserRoleEnum.ADMIN || user.getUsername().equals(comment.getUser().getUsername())) {
                    //댓글 단 유저와 로그인 유저가 같거나 권한이 ADMIN 일 때
                    commentRepository.deleteById(id);
                    return ResponseDto.success("success");
                }else {
                    throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
                }
            }
        }
        throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
    }
}
