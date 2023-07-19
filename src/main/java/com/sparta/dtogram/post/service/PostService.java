package com.sparta.dtogram.post.service;

import com.sparta.dtogram.post.entity.PostLike;
import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.dto.PostsResponseDto;
import com.sparta.dtogram.post.dto.UpdatePostRequestDto;
import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.post.repository.PostLikeRepository;
import com.sparta.dtogram.post.repository.PostRepository;
import com.sparta.dtogram.post.entity.PostTag;
import com.sparta.dtogram.post.repository.PostTagRepository;
import com.sparta.dtogram.tag.entity.Tag;
import com.sparta.dtogram.tag.repository.TagRepository;
import com.sparta.dtogram.user.entity.User;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final PostLikeRepository postLikeRepository;

    // 게시글 생성
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        log.info("게시글 생성 시도");

        try {
            log.info("게시글 생성 성공");
            Post post = postRepository.save(new Post(requestDto, user));
            return new PostResponseDto(post);
        } catch (RejectedExecutionException e) {
            log.error("게시글 생성 실패", e);
            throw new RuntimeException("Fail ! 게시글 생성 실패", e);
        }
    }

    // 게시글 단건 조회
    @Transactional(readOnly = true)
    public PostResponseDto getPostById(Long id) {
        Post post = findPost(id);

        return new PostResponseDto(post);
    }

    // 게시글 다건 조회
    @Transactional(readOnly = true)
    public PostsResponseDto getPosts() {
        List<PostResponseDto> posts = postRepository.findAllByOrderByModifiedAtDesc().stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new PostsResponseDto(posts);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, UpdatePostRequestDto requestDto, User user) {
        Post post = findPost(id);
        if (post.getUser().getNickname().equals(user.getNickname())) {
            post.updatePost(requestDto);
        } else {
            throw new RuntimeException("Exception ! 작성자가 아닌 게시글 수정 시도 감지");
        }
        return new PostResponseDto(post);
    }

    @Transactional
    public void deletePost(Long id, User user) {
        Post post = findPost(id);
        if (post.getUser().getNickname().equals(user.getNickname())) {
            postRepository.delete(post);
        } else {
            throw new RuntimeException("Exception ! 작성자가 아닌 게시글 삭제 시도 감지");
        }
    }

    @Transactional
    public void createPostLike(Long id, User user) {
        log.info("게시글 좋아요 누르기 시도");
        Post post = findPost(id);

        if(postLikeRepository.findByUserAndPost(user, post).isPresent()) {
            log.info("게시글 좋아요 누르기 실패");
            throw new DuplicateRequestException("Exception ! 동일한 사용자의 좋아요 중복선택 시도 감지");
        } else {
            log.info("게시글 좋아요 누르기 성공");
            PostLike postLike = new PostLike(user, post);
            post.registerPostLike(postLike);
            postLikeRepository.save(postLike);
        }
    }

    @Transactional
    public void deletePostLike(Long id, User user) {
        Post post = findPost(id);
        Optional<PostLike> postLike = postLikeRepository.findByUserAndPost(user, post);

        if(postLike.isPresent()) {
            post.cancelPostLike(postLike.get());
            postLikeRepository.delete(postLike.get());
        } else {
            throw new IllegalArgumentException("Exception ! 존재하지 않는 게시글에 대한 좋아요 누르기 시도 감지");
        }
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Exception ! 존재하지 않는 게시글 찾기 시도 감지")
        );
    }

    public void addTag(Long postId, Long tagId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new IllegalArgumentException("선택한 글은 존재하지 않습니다.")
        );
        Tag tag = tagRepository.findById(tagId).orElseThrow(() ->
                new IllegalArgumentException("선택한 태그는 존재하지 않습니다.")
        );

        if (!post.getUser().getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("회원님의 글이 아닙니다.");
        }

        Optional<PostTag> overlapTag = postTagRepository.findByPostAndTag(post, tag);

        if (overlapTag.isPresent()){
            throw new IllegalArgumentException("중복된 태그입니다.");
        }
        postTagRepository.save(new PostTag(post, tag));
    }

    @Transactional(readOnly = true)
    public PostsResponseDto getPostsByTag(Long tagId) {
        List<Post> posts = postRepository.findAllByPostTags_TagId(tagId);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : posts) {
            postResponseDtos.add(new PostResponseDto(post));
        }

        return new PostsResponseDto(postResponseDtos);
    }
}
