package com.sparta.dtogram.post.repository;

import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.post.entity.PostTag;
import com.sparta.dtogram.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    Optional<PostTag> findByPostAndTag(Post post, Tag tag);

}
