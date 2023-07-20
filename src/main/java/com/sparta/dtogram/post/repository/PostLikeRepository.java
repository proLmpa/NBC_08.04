package com.sparta.dtogram.post.repository;

import com.sparta.dtogram.post.entity.PostLike;
import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);

    List<PostLike> findAllByPost_id(Long id);
}
