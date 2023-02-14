package com.sparta.homeworkjwt.repository;

import com.sparta.homeworkjwt.entity.Post;
import com.sparta.homeworkjwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

}
