package com.sparta.homeworkjwt.service;


import com.sparta.homeworkjwt.component.JwtUtil;
import com.sparta.homeworkjwt.dto.PostRequestDto;
import com.sparta.homeworkjwt.dto.PostResponseDto;
import com.sparta.homeworkjwt.dto.ResponseDto;
import com.sparta.homeworkjwt.entity.Post;
import com.sparta.homeworkjwt.entity.PostLike;
import com.sparta.homeworkjwt.entity.User;
import com.sparta.homeworkjwt.entity.UserRoleEnum;
import com.sparta.homeworkjwt.repository.CommentRepository;
import com.sparta.homeworkjwt.repository.PostLikeRepository;
import com.sparta.homeworkjwt.repository.PostRepository;
import com.sparta.homeworkjwt.repository.UserRepository;
import com.sparta.homeworkjwt.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final PostLikeRepository postLikeRepository;


    private final CommentRepository commentRepository;

    public ResponseDto<List<PostResponseDto>> getPosts() {
        List<Post> list = postRepository.findAll();
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        for(Post post : list) {
            postResponseDtoList.add(new PostResponseDto(post));
        }
        return ResponseDto.success(postResponseDtoList);
    }

    public ResponseDto<PostResponseDto> createPost(PostRequestDto postRequestDto, UserDetailsImpl userDetails) {
            Post post = new Post(postRequestDto, userDetails.getUser());
            postRepository.save(post);
            return ResponseDto.success(new PostResponseDto(post));

    }


    public ResponseDto<PostResponseDto> getPost(Long id) {
        PostResponseDto postResponseDto = new PostResponseDto(postRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("존재하지 않는 게시글")));
        return ResponseDto.success(postResponseDto);
    }

    @Transactional
    public ResponseDto<PostResponseDto> updatePost(Long id, PostRequestDto postRequestDto, UserDetailsImpl userDetails) {
            User user = userDetails.getUser();
            Post post = postRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("존재하지 않는 게시글"));
            if(user.getRole() == UserRoleEnum.ADMIN || user.getUsername().equals(post.getUser().getUsername())) {
                // 게시글 작성자와 로그인 유저가 같거나 로그인 유저가 어드민일 때
                post.update(postRequestDto);
                return ResponseDto.success(new PostResponseDto(post));
            } else {
                throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
            }
    }

    public ResponseDto<String> deletePost(Long id, UserDetailsImpl userDetails) {
                User user = userDetails.getUser();
                Post post = postRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("존재하지 않는 게시글"));
                if(user.getRole() == UserRoleEnum.ADMIN || user.getUsername().equals(post.getUser().getUsername())) {
                    // 게시글 작성자와 로그인 유저가 같거나 어드민일때
                    postRepository.deleteById(id);
                }else {
                    throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
                }
                return ResponseDto.success("success");
    }

    public ResponseDto<String> likePost(Long id, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(id).orElseThrow( () -> new NullPointerException("존재하지 않는 게시글"));
        Optional<PostLike> optionalPostLike = postLikeRepository.findByPostAndUser(post, userDetails.getUser());
        if(optionalPostLike.isPresent()) { // 유저가 이미 좋아요를 눌렀을 때
            postLikeRepository.deleteById(optionalPostLike.get().getId());
            return ResponseDto.success("게시글 좋아요 취소");
        }

        postLikeRepository.save(new PostLike(post, userDetails.getUser()));
        return ResponseDto.success("게시글 좋아요 성공");
    }
}
