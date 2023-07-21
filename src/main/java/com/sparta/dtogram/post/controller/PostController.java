package com.sparta.dtogram.post.controller;

import com.sparta.dtogram.common.dto.MsgResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.post.dto.PostsResponseDto;
import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.service.PostService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    // 게시글 생성
    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PostRequestDto requestDto) {
        log.info("게시글 생성 시도");
        try {
            PostResponseDto result = postService.createPost(requestDto, userDetails.getUser());
            log.info("게시글 생성 성공");
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            log.error("게시글 생성 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 게시글 단건 조회
    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        PostResponseDto result = postService.getPostById(id);
        return ResponseEntity.ok().body(result);
    }
    
    // 게시글 다건 조회
    @GetMapping("/post")
    public ResponseEntity<PostsResponseDto> getPosts() {
        PostsResponseDto result = postService.getPosts();
        return ResponseEntity.ok().body(result);
    }

    // 게시글 수정
    @PutMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto result = postService.updatePost(id, requestDto, userDetails.getUser());

        return ResponseEntity.ok().body(result);
    }

    // 게시글 삭제
    @DeleteMapping("/post/{id}")
    public ResponseEntity<MsgResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("게시글 삭제 시도");
        try {
            postService.deletePost(id, userDetails.getUser());
            return ResponseEntity.ok().body(new MsgResponseDto("게시글 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new MsgResponseDto("작성자만 삭제 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 게시글에 좋아요 등록/해제하기
    @PostMapping("/post/{id}/like")
    public ResponseEntity<MsgResponseDto> createPostLike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        try {
            int likes = postService.likePost(id, userDetails.getUser());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MsgResponseDto("좋아요 "+likes, HttpStatus.ACCEPTED.value()));
        } catch(DuplicateRequestException e) {
            return ResponseEntity.badRequest().body(new MsgResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    // 태그 추가
    @PostMapping("/post/{postId}/tag/{tagId}")
    public ResponseEntity<MsgResponseDto> addTag(@PathVariable Long postId, @PathVariable Long tagId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.addTag(postId, tagId, userDetails.getUser());
        return ResponseEntity.ok().body(new MsgResponseDto("해시태그 추가 성공", HttpStatus.OK.value()));
    }

    // 태그에 맞는 글만 조회
    @GetMapping("/post/tag/{tagId}")
    public ResponseEntity<PostsResponseDto> getPostsByTag(@PathVariable Long tagId) {
        PostsResponseDto result = postService.getPostsByTag(tagId);
        return ResponseEntity.ok().body(result);
    }
}
