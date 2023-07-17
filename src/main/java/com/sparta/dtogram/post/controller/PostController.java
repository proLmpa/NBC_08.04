package com.sparta.dtogram.post.controller;

import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.service.PostService;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/blog")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails.getUser());
    }

    @GetMapping("/blogs")
    public List<PostResponseDto> getBlogs() {
        return postService.getPosts();
    }

    @GetMapping("/posts/find")
    public List<PostResponseDto> getpostsByKeyword(@RequestParam String keyword) {
        return postService.getPostsByKeyword(keyword);
    }

    @PutMapping("/post")
    public PostResponseDto updatepost(@RequestParam Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/post")
    public ResponseEntity<String> deletepost(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(id, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body("글 삭제 성공");
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
