package com.sparta.dtogram.post.controller;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.dto.PostsResponseDto;
import com.sparta.dtogram.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "게시글 API", description = "게시글 생성/조회/수정/삭제 및 게시글 좋아요 등록/해제, 태그 등록/해제, 그리고 좋아요별 태그별 팔로우별 팔로워별 조회를 위한 API 정보를 담고 있습니다.")
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글 생성", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 PostRequestDto와 MultipartFile을 통해 게시글 생성 후 반환합니다.")
    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart("requestDto") PostRequestDto requestDto, @RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        log.info("게시글 생성");
        PostResponseDto result = postService.createPost(requestDto, userDetails.getUser(), multipartFile);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "단일 게시글 조회", description = "게시글 ID를 통해 존재 유무 확인 후 존재 시 선택된 게시글을 반환합니다.")
    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        log.info("단일 게시글 조회");
        PostResponseDto result = postService.getPostById(id);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "전체 게시글 조회", description = "sortBy 정렬 기준을 바탕으로 오름차/내림차를 적용하여 size 크기 만큼 분할 후 page에 포함된 게시글들을 반환합니다.")
    @GetMapping("/post")
    public ResponseEntity<PostsResponseDto> getPosts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc) {
        log.info("전체 게시글 조회");
        PostsResponseDto result = postService.getPosts(page - 1, size, sortBy, isAsc);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "게시글 수정", description = "전달된 Bearer 토큰을 통해 본인 확인 및 게시글 ID를 통해 존재 유무 확인 후 PostRequestDto와 MultipartFile을 바탕으로 기존 게시글을 수정합니다.")
    @PutMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart("requestDto") PostRequestDto requestDto, @RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
        log.info("게시글 수정");
        PostResponseDto result = postService.updatePost(id, requestDto, userDetails.getUser(), multipartFile);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "게시글 삭제", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 게시글 ID를 통해 존재 유무 확인 후 존재 시 선택된 게시글을 삭제합니다.")
    @DeleteMapping("/post/{id}")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 삭제");
        postService.deletePost(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("게시글 삭제 성공", HttpStatus.OK.value()));
    }

    @Operation(summary = "게시글 좋아요 등록하기", description = "전달된 Bearer 토큰을 통해 본인 확인 및 게시글 ID를 통해 존재 유무 확인 후 둘 다 존재 시 좋아요를 등록합니다.")
    @PostMapping("/post/{id}/like")
    public ResponseEntity<ApiResponseDto> submitLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 좋아요 등록하기");
        postService.submitLike(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("게시글 좋아요 등록 성공", HttpStatus.OK.value()));
    }

    @Operation(summary = "좋아요 기반 전체 게시글 조회", description = "전달된 Bearer 토큰을 통해 본인 확인 후 본인이 좋아요를 등록한 게시글을 모두 조회합니다.")
    @GetMapping("/post/likes")
    public ResponseEntity<PostsResponseDto> getPostsByLike(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("좋아요 기반 게시글 조회 기능");
        PostsResponseDto result = postService.getPostsByLike(userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "게시글 좋아요 삭제하기", description = "전달된 Bearer 토큰을 통해 본인 확인 및 게시글 ID를 통해 존재 유무 확인 후 둘 다 존재 시 기존의 좋아요를 해제합니다.")
    @DeleteMapping("/post/{id}/like")
    public ResponseEntity<ApiResponseDto> cancelLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 좋아요 삭제하기");
        postService.cancelLike(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("게시글 좋아요 삭제 성공", HttpStatus.OK.value()));
    }

    @Operation(summary = "게시글 태그 등록하기", description = "전달된 Bearer 토큰을 통해 본인 확인 및 게시글과 태그 ID를 통해 존재 유무 확인 후 모든 조건 충족 시 게시글에 태그를 등록합니다.")
    @PostMapping("/post/{postId}/tag/{tagId}")
    public ResponseEntity<ApiResponseDto> submitTag(@PathVariable Long postId, @PathVariable Long tagId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 태그 추가");
        postService.submitTag(postId, tagId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("게시글 태그 추가 성공", HttpStatus.OK.value()));
    }

    @Operation(summary = "태그 기반 전체 게시글 조회", description = "태그 ID를 통해 해당 태그의 존재 여부 확인 후 해당 태그가 포함된 게시글을 모두 조회합니다.")
    @GetMapping("/post/tag/{tagId}")
    public ResponseEntity<PostsResponseDto> getPostsByTag(@PathVariable Long tagId) {
        log.info("태그 기반 게시물 조회");
        PostsResponseDto result = postService.getPostsByTag(tagId);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "게시글 태그 삭제하기", description = "전달된 Bearer 토큰을 통해 본인 확인 및 게시글과 태그 ID를 통해 존재 유무 확인 후 모든 조건 충족 시 게시글에 등록됐던 해당 태그를 삭제합니다.")
    @DeleteMapping("/post/{postId}/tag/{tagId}")
    public ResponseEntity<ApiResponseDto> addTag(@PathVariable Long postId, @PathVariable Long tagId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 태그 삭제");
        postService.cancelTag(postId, tagId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("게시글 태그 삭제 성공", HttpStatus.OK.value()));
    }

    @Operation(summary = "자신을 팔로우 중인 사용자의 전체 게시글 목록 생성", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 본인을 팔로우한 사용자들의 모든 게시글을 size 크기만 큼 분할 후 원하는 page의 게시글들을 조회합니다.")
    @GetMapping("/post/followers")
    public ResponseEntity<PostsResponseDto> getFollowersPosts(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        log.info("자신을 팔로우 중인 사용자의 전체 게시글 목록 생성");
        PostsResponseDto result = postService.getFollowersPosts(page-1, size, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "자신이 팔로우 중인 사용자의 전체 게시글 목록 생성", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 본인이 팔로우한 사용자들의 모든 게시글을 size 크기만 큼 분할 후 원하는 page의 게시글들을 조회합니다.")
    @GetMapping("/post/followings")
    public ResponseEntity<PostsResponseDto> getFollowingsPosts(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        log.info("자신이 팔로우 중인 사용자의 전체 게시글 목록 생성");
        PostsResponseDto result = postService.getFollowingsPosts(page-1, size, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }
}