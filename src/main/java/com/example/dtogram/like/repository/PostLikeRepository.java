package com.example.dtogram.like.repository;

import com.example.dtogram.like.entity.PostLike;
import com.example.dtogram.post.entity.Post;
import com.example.dtogram.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
}
