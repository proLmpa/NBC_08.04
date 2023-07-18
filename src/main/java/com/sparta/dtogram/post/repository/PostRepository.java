package com.sparta.dtogram.post.repository;

import com.sparta.dtogram.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByModifiedAtDesc();
    List<Post> findAllByContentContainingOrderByModifiedAtDesc(String keyword);
}
