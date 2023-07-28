package com.sparta.dtogram.post.controller;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.dto.PostsResponseDto;
import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    // 게시글 생성
    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart("requestDto") PostRequestDto requestDto, @RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        log.info("게시글 생성");
        PostResponseDto result = postService.createPost(requestDto, userDetails.getUser(), multipartFile);
        return ResponseEntity.ok().body(result);
    }

    // 게시글 단건 조회
    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        log.info("단일 게시글 조회");
        PostResponseDto result = postService.getPostById(id);
        return ResponseEntity.ok().body(result);
    }

    // 게시글 다건 조회
    @GetMapping("/post")
    public ResponseEntity<PostsResponseDto> getPosts(Post post) {
        log.info("전체 게시글 조회");
        PostsResponseDto result = postService.getPosts();
        return ResponseEntity.ok().body(result);
    }

    // 게시글 수정
    @PutMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart("requestDto") PostRequestDto requestDto, @RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        log.info("게시글 수정");
        PostResponseDto result = postService.updatePost(id, requestDto, userDetails.getUser(), multipartFile);
        return ResponseEntity.ok().body(result);
    }

    // 게시글 삭제
    @DeleteMapping("/post/{id}")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 삭제");
        postService.deletePost(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("게시글 삭제 성공", HttpStatus.OK.value()));
    }

    // 게시글에 좋아요 등록/해제하기
    @PostMapping("/post/{id}/like")
    public ResponseEntity<ApiResponseDto> createPostLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 좋아요");
        int likes = postService.likePost(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("좋아요 " + likes, HttpStatus.OK.value()));
    }

    // 태그 추가
    @PostMapping("/post/{postId}/tag/{tagId}")
    public ResponseEntity<ApiResponseDto> addTag(@PathVariable Long postId, @PathVariable Long tagId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 태그 추가");
        postService.addTag(postId, tagId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("해시태그 추가 성공", HttpStatus.OK.value()));
    }

    // 태그에 맞는 글만 조회
    @GetMapping("/post/tag/{tagId}")
    public ResponseEntity<PostsResponseDto> getPostsByTag(@PathVariable Long tagId) {
        PostsResponseDto result = postService.getPostsByTag(tagId);
        return ResponseEntity.ok().body(result);
    }
}