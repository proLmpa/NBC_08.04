package com.sparta.dtogram.post.service;

import com.sparta.dtogram.common.error.DtogramErrorCode;
import com.sparta.dtogram.common.exception.DtogramException;
import com.sparta.dtogram.common.service.S3Uploader;
import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.dto.PostsResponseDto;
import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.post.entity.PostLike;
import com.sparta.dtogram.post.entity.PostTag;
import com.sparta.dtogram.post.repository.PostLikeRepository;
import com.sparta.dtogram.post.repository.PostRepository;
import com.sparta.dtogram.post.repository.PostTagRepository;
import com.sparta.dtogram.tag.entity.Tag;
import com.sparta.dtogram.tag.repository.TagRepository;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.entity.UserRoleEnum;
import com.sparta.dtogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final PostLikeRepository postLikeRepository;
    private final S3Uploader s3Uploader;

    // 게시글 생성
    public PostResponseDto createPost(PostRequestDto requestDto, User user, MultipartFile multipartFile) throws IOException {
        try {
            String storedFileName = s3Uploader.upload(multipartFile, "postFile");
            Post post = postRepository.save(new Post(requestDto, user, storedFileName));

            return new PostResponseDto(post);
        } catch (RejectedExecutionException e) {
            throw new DtogramException(DtogramErrorCode.S3_UPLOAD_FAILURE, null);
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
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user, MultipartFile multipartFile) throws IOException{
        Post post = findPost(id);

        if (matchUser(post, user)) {
            String storedFileName = multipartFile.isEmpty() ? post.getMultiMediaUrl() : s3Uploader.upload(multipartFile, "postFile");
            post.updatePost(requestDto, storedFileName);
        } else {
            throw new DtogramException(DtogramErrorCode.UNAUTHORIZED_USER, null);
        }

        return new PostResponseDto(post);
    }

    @Transactional
    public void deletePost(Long id, User user) {
        Post post = findPost(id);
        if (matchUser(post, user)) {
            postRepository.delete(post);
        } else {
            throw new DtogramException(DtogramErrorCode.UNAUTHORIZED_USER, null);
        }
    }

    @Transactional
    public int likePost(Long id, User user) {
        User foundUser = findUser(user);
        Post post = findPost(id);
        PostLike postLike = postLikeRepository.findByUserAndPost(foundUser, post).orElse(null);

        if(postLike == null) {
            postLike = new PostLike(foundUser, post);
            postLikeRepository.save(postLike);
        } else {
            postLike.cancelLike();
            postLikeRepository.delete(postLike);
        }

        List<PostLike> postLikes = postLikeRepository.findAllByPost_id(post.getId());
        return postLikes.size();
    }

    public void addTag(Long postId, Long tagId, User user) {
        Post post = findPost(postId);
        Tag tag = findTag(tagId);

        if (matchUser(post, user)) {
            postTagRepository.findByPostAndTag(post, tag).orElseThrow(
                    () -> new DtogramException(DtogramErrorCode.TAG_NOT_FOUND, null)
            );

            postTagRepository.save(new PostTag(post, tag));
        } else {
            throw new DtogramException(DtogramErrorCode.UNAUTHORIZED_USER, null);
        }
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

    private User findUser(User user) {
        return userRepository.findByUsername(user.getUsername()).orElseThrow(() ->
                new DtogramException(DtogramErrorCode.USER_NOT_FOUND, null)
        );
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new DtogramException(DtogramErrorCode.POST_NOT_FOUND, null)
        );
    }

    private Tag findTag(Long id) {
        return tagRepository.findById(id).orElseThrow(() ->
                new DtogramException(DtogramErrorCode.TAG_NOT_FOUND, null)
        );
    }

    private boolean matchUser(Post post, User user) {
        return post.getUser().getId().equals(user.getId()) || user.getRole().equals(UserRoleEnum.ADMIN);
    }
}