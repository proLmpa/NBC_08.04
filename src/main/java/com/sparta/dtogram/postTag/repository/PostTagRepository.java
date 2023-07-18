package com.sparta.dtogram.postTag.repository;

import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.postTag.entity.PostTag;
import com.sparta.dtogram.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    Optional<PostTag> findByPostAndTag(Post post, Tag tag);

    List<Post> findByTagId(Long tagId);
}
