package com.sparta.dtogram.like.repository;

import com.sparta.dtogram.like.entity.PostLike;
import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
}
