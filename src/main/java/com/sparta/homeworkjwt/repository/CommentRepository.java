package com.sparta.homeworkjwt.repository;

import com.sparta.homeworkjwt.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPostId (Long id);

}
