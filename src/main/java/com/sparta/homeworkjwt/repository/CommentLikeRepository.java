package com.sparta.homeworkjwt.repository;

import com.sparta.homeworkjwt.entity.Comment;
import com.sparta.homeworkjwt.entity.CommentLike;
import com.sparta.homeworkjwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {


    Optional<CommentLike> findByCommentAndUser(Comment comment, User user);
}
