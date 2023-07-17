package com.example.dtogram.post.service;

import com.example.dtogram.like.repository.PostLikeRepository;
import com.example.dtogram.post.dto.PostRequestDto;
import com.example.dtogram.post.dto.PostResponseDto;
import com.example.dtogram.post.entity.Post;
import com.example.dtogram.post.repository.PostRepository;
import com.example.dtogram.user.entity.User;
import com.example.dtogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post= postRepository.save(new Post(requestDto, user));

        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getPostsByKeyword(String keyword) {
        if (keyword == null) {
            throw new RuntimeException("키워드를 입력해주세요");
        }
        return postRepository.findAllByContentsContainingOrderByModifiedAtDesc(keyword).stream().map(PostResponseDto::new).toList();
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        Post post = findPost(id);
        if (post.getUsername().equals(user.getUsername())) {
            post.update(requestDto);
        } else {
            throw new RuntimeException("작성자만 삭제/수정할 수 있습니다.");
        }
        return new PostResponseDto(post);
    }


    public void deletePost(Long id, User user) {
        Post post = findPost(id);
        if (post.getUsername().equals(user.getUsername())) {
            postRepository.delete(post);
        } else {
            throw new RuntimeException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> // null 체크
                new IllegalArgumentException("선택한 글는 존재하지 않습니다.")
        );
    }

//    private User findUser(Long id) {
//        return userRepository.findById(id).orElseThrow(() ->
//                new IllegalArgumentException("존재하지 않는 유저입니다.")
//        );
//    }

//    @Transactional
//    public String like(Long postId, Long userId) {
//        final String[] msg = {""};
//
//        Post post = findPost(postId);
//        User user = findUser(userId);
//
//        Optional<PostLike> isLike = postLikeRepository.findByUserAndPost(user, post);
//
//        isLike.ifPresentOrElse(
//                like -> {
//                    postLikeRepository.delete(like);
//                    post.subLikeCount(like);
//                    post.updateLikeCount();
//                    msg[0] = "좋아요 취소";
//                },
//                () -> {
//                    PostLike postLike = new PostLike(user, post);
//
//                    postLike.mappingPost(post);
//                    postLike.mappingUser(user);
//                    post.updateLikeCount();
//
//                    postLikeRepository.save(postLike);
//                    msg[0] = "좋아요";
//                }
//        );
//        return msg[0];
//    }
//
//    public boolean isLiked(Long postId, Long userId) {
//        Post post = findPost(postId);
//        User user = userRepository.findById(userId).orElse(new User());
//        Optional<PostLike> isLike = postLikeRepository.findByUserAndPost(user, post);
//        boolean isLiked = PostLike.isLikedPost(isLike);
//        return isLiked;
//    }
}
