package com.sparta.dtogram.post.service;

import com.sparta.dtogram.common.error.DtogramErrorCode;
import com.sparta.dtogram.common.exception.DtogramException;
import com.sparta.dtogram.common.service.S3Uploader;
import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.dto.PostsResponseDto;
import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.like.post.entity.PostLike;
import com.sparta.dtogram.tag.entity.PostTag;
import com.sparta.dtogram.like.post.repository.PostLikeRepository;
import com.sparta.dtogram.post.repository.PostRepository;
import com.sparta.dtogram.tag.repository.PostTagRepository;
import com.sparta.dtogram.tag.entity.Tag;
import com.sparta.dtogram.tag.repository.TagRepository;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.entity.UserRoleEnum;
import com.sparta.dtogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final PostLikeRepository postLikeRepository;
    private final S3Uploader s3Uploader;

    // 게시글 생성
    @Override
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, User user, MultipartFile multipartFile) throws IOException {
        try {
            String storedFileName = s3Uploader.uploadFile(multipartFile, "").getBody();
            Post post = postRepository.save(new Post(requestDto, user, storedFileName));

            return new PostResponseDto(post);
        } catch (RejectedExecutionException e) {
            throw new DtogramException(DtogramErrorCode.S3_UPLOAD_FAILURE, null);
        }
    }

    // 게시글 단건 조회
    @Override
    @Transactional(readOnly = true)
    public PostResponseDto getPostById(Long id) {
        Post post = findPost(id);
        return new PostResponseDto(post);
    }

    // 게시글 다건 조회
    @Override
    @Transactional(readOnly = true)
    public PostsResponseDto getPosts(int page, int size, String sortBy, boolean isAsc) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        List<PostResponseDto> posts = postRepository.findAll(pageable).stream()
                .map(PostResponseDto::new).toList();

        return new PostsResponseDto(posts);
    }

    // 게시글 수정
    @Override
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user, MultipartFile multipartFile) throws IOException{
        Post post = findPost(id);

        if (matchUser(post, user)) {
            String storedFileName = s3Uploader.uploadFile(multipartFile, post.getMultiMediaUrl()).getBody();
            post.updatePost(requestDto, storedFileName);
        } else {
            throw new DtogramException(DtogramErrorCode.UNAUTHORIZED_USER, null);
        }

        return new PostResponseDto(post);
    }

    // 게시글 삭제
    @Override
    @Transactional
    public void deletePost(Long id, User user) {
        Post post = findPost(id);
        if (matchUser(post, user)) {
            postRepository.delete(post);
        } else {
            throw new DtogramException(DtogramErrorCode.UNAUTHORIZED_USER, null);
        }
    }

    // 게시글 좋아요 등록
    @Override
    @Transactional
    public void submitLike(Long id, User user) {
        User foundUser = findUser(user);
        Post post = findPost(id);
        PostLike postLike = findPostLike(post, foundUser);

        if(postLike == null) {
            postLikeRepository.save(new PostLike(foundUser, post));
        } else {
            throw new DtogramException(DtogramErrorCode.LIKE_ALREADY_EXISTS, null);
        }
    }

    // 좋아요 기반 게시물 조회
    @Override
    @Transactional(readOnly = true)
    public PostsResponseDto getPostsByLike(User user){
        User foundUser = findUser(user);

        List<PostResponseDto> likedPosts = postRepository.getLikedPostsByUserId(foundUser.getId())
                .stream().map(PostResponseDto::new).toList();

        return new PostsResponseDto(likedPosts);
    }

    // 게시글 좋아요 해제
    @Override
    @Transactional
    public void cancelLike(Long id, User user) {
        User foundUser = findUser(user);
        Post post = findPost(id);
        PostLike postLike = findPostLike(post, foundUser);

        if(postLike != null) {
            postLikeRepository.delete(postLike);
        } else {
            throw new DtogramException(DtogramErrorCode.LIKE_NOT_FOUND, null);
        }
    }

    // 게시글 태그 추가
    @Override
    @Transactional
    public void submitTag(Long postId, Long tagId, User user) {
        Post post = findPost(postId);
        Tag tag = findTag(tagId);

        if (matchUser(post, user)) {
            PostTag searchPostTag = findPostTag(post, tag);
            if(searchPostTag == null){
                postTagRepository.save(new PostTag(post, tag));
            } else {
                throw new DtogramException(DtogramErrorCode.POST_TAG_ALREADY_EXISTS, null);
            }
        } else {
            throw new DtogramException(DtogramErrorCode.UNAUTHORIZED_USER, null);
        }
    }

    // 태그 기반 게시물 조회
    @Override
    @Transactional(readOnly = true)
    public PostsResponseDto getPostsByTag(Long tagId) {
        // 존재하지 않는 태그에 대해 검색을 실행하는 경우를 방지하기 위해 실행
        findTag(tagId);

        List<PostResponseDto> tagedPosts = postRepository.getPostsByTagId(tagId)
                .stream().map(PostResponseDto::new).toList();

        return new PostsResponseDto(tagedPosts);
    }

    // 게시글 태그 삭제
    @Override
    public void cancelTag(Long postId, Long tagId, User user) {
        Post post = findPost(postId);
        Tag tag = findTag(tagId);

        if (matchUser(post, user)) {
            PostTag searchPostTag = findPostTag(post, tag);
            if(searchPostTag != null){
                postTagRepository.delete(searchPostTag);
            } else {
                throw new DtogramException(DtogramErrorCode.POST_TAG_NOT_FOUND, null);
            }
        } else {
            throw new DtogramException(DtogramErrorCode.UNAUTHORIZED_USER, null);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PostsResponseDto getFollowersPosts(int page, int size, User user) {
        Pageable pageable = PageRequest.of(page, size);

        List<PostResponseDto> followersPosts = postRepository
                .getFollowersPostsByUserId(user.getId(), pageable)
                .stream().map(PostResponseDto::new).toList();

        return new PostsResponseDto(followersPosts);
    }

    @Override
    @Transactional(readOnly = true)
    public PostsResponseDto getFollowingsPosts(int page, int size, User user) {
        Pageable pageable = PageRequest.of(page, size);

        List<PostResponseDto> followersPosts = postRepository
                .getFollowingsPostsByUserId(user.getId(), pageable)
                .stream().map(PostResponseDto::new).toList();

        return new PostsResponseDto(followersPosts);
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

    private PostLike findPostLike(Post post, User user){
        return postLikeRepository.findByUserAndPost(user, post).orElse(null);
    }

    private PostTag findPostTag(Post post, Tag tag){
        return postTagRepository.findByPostAndTag(post, tag).orElse(null);
    }

    private boolean matchUser(Post post, User user) {
        return post.getUser().getId().equals(user.getId()) || user.getRole().equals(UserRoleEnum.ADMIN);
    }
}