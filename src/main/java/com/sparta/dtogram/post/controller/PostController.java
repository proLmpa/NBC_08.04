package com.sparta.dtogram.post.controller;

import com.sparta.dtogram.common.dto.MsgResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.post.dto.PostListResponseDto;
import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.dto.UpdatePostRequestDto;
import com.sparta.dtogram.post.service.PostService;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    // 게시글 생성
    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto result = postService.createPost(requestDto, userDetails.getUser());
        return ResponseEntity.status(201).body(result);    }

    // 게시글 단건 조회
    @GetMapping("/post")
    public ResponseEntity<PostResponseDto> getPostById(@RequestParam Long id) {
        PostResponseDto result = postService.getPostById(id);
        return ResponseEntity.ok().body(result);
    }
    
    // 게시글 다건 조회
    @GetMapping("/post/all")
    public ResponseEntity<PostListResponseDto> getPosts() {
        PostListResponseDto result = postService.getPosts();
        return ResponseEntity.ok().body(result);
    }

    // 키워드별 게시글 다건 조회

    // 게시글 수정
    @PutMapping("/post")
    public ResponseEntity<PostResponseDto> updatePost(@RequestParam Long id, @RequestBody UpdatePostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto result = postService.updatePost(id, requestDto, userDetails.getUser());

        return ResponseEntity.ok().body(result);
    }

    // 게시글 삭제
    @DeleteMapping("/post")
    public ResponseEntity<MsgResponseDto> deletePost(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            postService.deletePost(id, userDetails.getUser());
            return ResponseEntity.ok().body(new MsgResponseDto("게시글 삭제 성공", HttpStatus.OK.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.badRequest().body(new MsgResponseDto("작성자만 삭제 할 수 있습니다.", HttpStatus.BAD_REQUEST.value()));
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
    public ResponseEntity<PostListResponseDto> getPostsByTag(@PathVariable Long tagId) {
        PostListResponseDto result = postService.getPostsByTag(tagId);
        return ResponseEntity.ok().body(result);
    }

//    @PostMapping("/post/like")
//    public String like(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return postService.like(id, userDetails.getUser().getId());
//    }
//
//    @GetMapping("/post/like")

//    public boolean isLiked(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return postService.isLiked(id, userDetails.getUser().getId());
//    }
}
