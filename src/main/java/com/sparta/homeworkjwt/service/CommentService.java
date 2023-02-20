package com.sparta.homeworkjwt.service;


import com.google.gson.Gson;
import com.sparta.homeworkjwt.component.JwtUtil;
import com.sparta.homeworkjwt.dto.CommentRequestDto;
import com.sparta.homeworkjwt.dto.CommentResponseDto;
import com.sparta.homeworkjwt.dto.ResponseDto;
import com.sparta.homeworkjwt.entity.*;
import com.sparta.homeworkjwt.repository.CommentLikeRepository;
import com.sparta.homeworkjwt.repository.CommentRepository;
import com.sparta.homeworkjwt.repository.PostRepository;
import com.sparta.homeworkjwt.repository.UserRepository;
import com.sparta.homeworkjwt.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final CommentLikeRepository commentLikeRepository;
    public ResponseDto<CommentResponseDto> createComment(Long id, String content, UserDetailsImpl userDetails) {
            Post post = postRepository.findById(id).orElseThrow( () -> new NullPointerException("등록되지 않은 게시글ID"));
            Comment comment = new Comment(userDetails.getUser(), post, content);
            commentRepository.save(comment);
            return ResponseDto.success(new CommentResponseDto(comment));
    }

    @Transactional
    public ResponseDto<CommentResponseDto> updateComment(Long id, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
            User user = userDetails.getUser();
            Comment comment = commentRepository.findById(id).orElseThrow(() -> new NullPointerException("댓글을 찾을 수 없음"));
            if(user.getRole() == UserRoleEnum.ADMIN || user.getUsername().equals(comment.getUser().getUsername())) {
                //댓글 단 유저와 로그인 유저가 같거나 권한이 ADMIN 일 때
                comment.update(commentRequestDto);
                return ResponseDto.success(new CommentResponseDto(comment));
            }else {
                throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
            }
    }

    public ResponseDto<String> deleteComment(Long id, UserDetailsImpl userDetails) {
            User user = userDetails.getUser();
            Comment comment = commentRepository.findById(id).orElseThrow(() -> new NullPointerException("댓글을 찾을 수 없음"));

            if(user.getRole() == UserRoleEnum.ADMIN || user.getUsername().equals(comment.getUser().getUsername())) {
                //댓글 단 유저와 로그인 유저가 같거나 권한이 ADMIN 일 때
                commentRepository.deleteById(id);
                return ResponseDto.success("success");
            }else {
                throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
            }
    }

    public ResponseDto<String> likeComment(Long id, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(id).orElseThrow( () -> new NullPointerException("존재하지 않는 댓글"));
        Optional<CommentLike> optionalCommentLike = commentLikeRepository.findByCommentAndUser(comment, userDetails.getUser());
        if(optionalCommentLike.isPresent()) { // 유저가 이미 좋아요를 눌렀을 때
            commentLikeRepository.deleteById(optionalCommentLike.get().getId());
            return ResponseDto.success("댓글 좋아요 취소");
        }

        commentLikeRepository.save(new CommentLike(comment, userDetails.getUser()));
        return ResponseDto.success("댓글 좋아요 성공");
    }
}
