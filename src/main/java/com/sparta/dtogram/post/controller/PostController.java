package com.sparta.dtogram.post.controller;

import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.dto.UpdatePostRequestDto;
import com.sparta.dtogram.post.service.PostService;
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
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @PostMapping("/Post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails.getUser());
    }

    @GetMapping("/Posts")
    public List<PostResponseDto> getPosts() {
        return postService.getPosts();
    }

    @GetMapping("/Posts/find")
    public List<PostResponseDto> getPostsByKeyword(@RequestParam String keyword) {
        return postService.getPostsByKeyword(keyword);
    }

    @PutMapping("/Post")
    public PostResponseDto updatePost(@RequestParam Long id, @RequestBody UpdatePostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/Post")
    public ResponseEntity<String> deletePost(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(id, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body("글 삭제 성공");
    }

//    @PostMapping("/Post/like")
//    public String like(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return postService.like(id, userDetails.getUser().getId());
//    }
//
//    @GetMapping("/Post/like")
//    public boolean isLiked(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return postService.isLiked(id, userDetails.getUser().getId());
//    }
}
