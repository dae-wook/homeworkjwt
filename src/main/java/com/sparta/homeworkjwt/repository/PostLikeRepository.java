package com.sparta.homeworkjwt.repository;

import com.sparta.homeworkjwt.entity.Post;
import com.sparta.homeworkjwt.entity.PostLike;
import com.sparta.homeworkjwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    void deleteByPostAndUser(Post post, User user);
    Optional<PostLike> findByPostAndUser(Post post, User user);

}
