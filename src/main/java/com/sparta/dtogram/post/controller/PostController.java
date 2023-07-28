package com.sparta.dtogram.post.controller;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.dto.PostsResponseDto;
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

    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart("requestDto") PostRequestDto requestDto, @RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        log.info("게시글 생성");
        PostResponseDto result = postService.createPost(requestDto, userDetails.getUser(), multipartFile);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        log.info("단일 게시글 조회");
        PostResponseDto result = postService.getPostById(id);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/post")
    public ResponseEntity<PostsResponseDto> getPosts() {
        log.info("전체 게시글 조회");
        PostsResponseDto result = postService.getPosts();
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart("requestDto") PostRequestDto requestDto, @RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        log.info("게시글 수정");
        PostResponseDto result = postService.updatePost(id, requestDto, userDetails.getUser(), multipartFile);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 삭제");
        postService.deletePost(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("게시글 삭제 성공", HttpStatus.OK.value()));
    }

    @PostMapping("/post/{id}/like")
    public ResponseEntity<ApiResponseDto> submitLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 좋아요 등록하기");
        postService.submitLike(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("게시글 좋아요 등록 성공", HttpStatus.OK.value()));
    }

    @DeleteMapping("/post/{id}/like")
    public ResponseEntity<ApiResponseDto> cancelLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 좋아요 삭제하기");
        postService.cancelLike(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("게시글 좋아요 삭제 성공", HttpStatus.OK.value()));
    }

    @PostMapping("/post/{postId}/tag/{tagId}")
    public ResponseEntity<ApiResponseDto> submitTag(@PathVariable Long postId, @PathVariable Long tagId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 태그 추가");
        postService.submitTag(postId, tagId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("게시글 태그 추가 성공", HttpStatus.OK.value()));
    }

    @GetMapping("/post/tag/{tagId}")
    public ResponseEntity<PostsResponseDto> getPostsByTag(@PathVariable Long tagId) {
        log.info("태그 기반 게시물 조회");
        PostsResponseDto result = postService.getPostsByTag(tagId);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/post/{postId}/tag/{tagId}")
    public ResponseEntity<ApiResponseDto> addTag(@PathVariable Long postId, @PathVariable Long tagId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 태그 삭제");
        postService.cancelTag(postId, tagId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("게시글 태그 삭제 성공", HttpStatus.OK.value()));
    }
}