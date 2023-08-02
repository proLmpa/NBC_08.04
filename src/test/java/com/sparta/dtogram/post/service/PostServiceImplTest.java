package com.sparta.dtogram.post.service;

import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.post.repository.PostRepository;
import com.sparta.dtogram.tag.entity.Tag;
import com.sparta.dtogram.tag.repository.PostTagRepository;
import com.sparta.dtogram.tag.repository.TagRepository;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.entity.UserRoleEnum;
import com.sparta.dtogram.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PostServiceImplTest {
    @Autowired
    PostServiceImpl postService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    PostTagRepository postTagRepository;

    @Test
    @Transactional
    @DisplayName("게시물에 태그 등록/조회/취소")
    void submitTag() {
        // given
        User user = new User("user2", "2resu", "user234@#$", "2resu@email.com", UserRoleEnum.ADMIN);
        user = userRepository.save(user);

        Post post = new Post("제목", "내용", user, "");
        post = postRepository.save(post);

        Tag tag = new Tag("Spring Boot", user);
        tag = tagRepository.save(tag);

        // when - 등록
        postService.submitTag(post.getId(), tag.getId(), user);

        // then - 확인
       assertEquals("제목", post.getTitle());
       assertEquals("Spring Boot", tag.getTagName());

        // when - 조회
        List<PostResponseDto> posts = postService.getPostsByTag(tag.getId()).getPosts();

        // then - 확인
        for(PostResponseDto response : posts) {
            assertEquals("제목", response.getTitle());
            assertEquals("내용", response.getContent());
        }

        // when - 삭제
        postService.cancelTag(post.getId(), tag.getId(), user);

        // then - 확인
    }
}