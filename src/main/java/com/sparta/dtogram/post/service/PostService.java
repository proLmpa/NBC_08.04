package com.sparta.dtogram.post.service;

import com.sparta.dtogram.post.entity.PostLike;
import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.dto.PostsResponseDto;
import com.sparta.dtogram.post.dto.UpdatePostRequestDto;
import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.post.repository.PostLikeRepository;
import com.sparta.dtogram.post.repository.PostRepository;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    // 게시글 생성
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post= postRepository.save(new Post(requestDto, user));
        return new PostResponseDto(post);
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

    // 게시글 다건 조회 (키워드별)
//    @Transactional(readOnly = true)
//    public List<PostResponseDto> getPostsByKeyword(String keyword) {
//        if (keyword == null) {
//            throw new RuntimeException("키워드를 입력해주세요");
//        }
//        return PostRepository.findAllByContentContainingOrderByModifiedAtDesc(keyword).stream().map(PostResponseDto::new).toList();
//    }

    @Transactional
    public PostResponseDto updatePost(Long id, UpdatePostRequestDto requestDto, User user) {
        Post post = findPost(id);
        if (post.getNickname().equals(user.getNickname())) {
            post.update(requestDto);
        } else {
            throw new RuntimeException("작성자만 삭제/수정할 수 있습니다.");
        }
        return new PostResponseDto(post);
    }

    @Transactional
    public void deletePost(Long id, User user) {
        Post post = findPost(id);
        if (post.getNickname().equals(user.getNickname())) {
            postRepository.delete(post);
        } else {
            throw new RuntimeException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시글")
        );
    }


    @Transactional
    public void likePost(Long id, User user) {
        Post post = findPost(id);

        if(postLikeRepository.existsByUserAndPost(user, post)) {
            throw new DuplicateRequestException("게시글 좋아요 중복선택");
        } else {
            PostLike postLike = new PostLike(user, post);
            postLikeRepository.save(postLike);
        }
    }

    @Transactional
    public void dislikePost(Long id, User user) {
        Post post = findPost(id);
        Optional<PostLike> postLike = postLikeRepository.findByUserAndPost(user, post);

        if(postLike.isPresent()) {
            postLikeRepository.delete(postLike.get());
        } else {
            throw new IllegalArgumentException("게시글 좋아요 취소 실패 : 취소할 좋아요가 없습니다.");
        }
    }
}
