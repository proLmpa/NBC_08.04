package com.sparta.dtogram.post.service;

import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.dto.UpdatePostRequestDto;
import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.post.repository.PostRepository;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository PostRepository;
//    private final PostLikeRepository PostLikeRepository;
    private final UserRepository userRepository;

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post Post= PostRepository.save(new Post(requestDto, user));

        return new PostResponseDto(Post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts() {
        return PostRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsByKeyword(String keyword) {
        if (keyword == null) {
            throw new RuntimeException("키워드를 입력해주세요");
        }
        return PostRepository.findAllByContentsContainingOrderByModifiedAtDesc(keyword).stream().map(PostResponseDto::new).toList();
    }

    @Transactional
    public PostResponseDto updatePost(Long id, UpdatePostRequestDto requestDto, User user) {
        Post Post = findPost(id);
        if (Post.getUsername().equals(user.getUsername())) {
            Post.update(requestDto);
        } else {
            throw new RuntimeException("작성자만 삭제/수정할 수 있습니다.");
        }
        return new PostResponseDto(Post);
    }


    public void deletePost(Long id, User user) {
        Post Post = findPost(id);
        if (Post.getUsername().equals(user.getUsername())) {
            PostRepository.delete(Post);
        } else {
            throw new RuntimeException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    private Post findPost(Long id) {
        return PostRepository.findById(id).orElseThrow(() -> // null 체크
                new IllegalArgumentException("선택한 글는 존재하지 않습니다.")
        );
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 유저입니다.")
        );
    }

//    @Transactional
//    public String like(Long PostId, Long userId) {
//        final String[] msg = {""};
//
//        Post Post = findPost(PostId);
//        User user = findUser(userId);
//
//        Optional<PostLike> isLike = PostLikeRepository.findByUserAndPost(user, Post);
//
//        isLike.ifPresentOrElse(
//                like -> {
//                    PostLikeRepository.delete(like);
//                    Post.subLikeCount(like);
//                    Post.updateLikeCount();
//                    msg[0] = "좋아요 취소";
//                },
//                () -> {
//                    PostLike PostLike = new PostLike(user, Post);
//
//                    PostLike.mappingPost(Post);
//                    PostLike.mappingUser(user);
//                    Post.updateLikeCount();
//
//                    PostLikeRepository.save(PostLike);
//                    msg[0] = "좋아요";
//                }
//        );
//        return msg[0];
//    }
//
//    public boolean isLiked(Long PostId, Long userId) {
//        Post Post = findPost(PostId);
//        User user = userRepository.findById(userId).orElse(new User());
//        Optional<PostLike> isLike = PostLikeRepository.findByUserAndPost(user, Post);
//        boolean isLiked = PostLike.isLikedPost(isLike);
//        return isLiked;
//    }
}
