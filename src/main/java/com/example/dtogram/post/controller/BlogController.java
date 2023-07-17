package com.sparta.myblogbackend.controller;

import com.sparta.myblogbackend.dto.BlogRequestDto;
import com.sparta.myblogbackend.dto.BlogResponseDto;
import com.sparta.myblogbackend.dto.UpdateBlogRequestDto;
import com.sparta.myblogbackend.entity.User;
import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.security.UserDetailsImpl;
import com.sparta.myblogbackend.service.BlogService;
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
public class BlogController {
    private final BlogService blogService;

    @PostMapping("/blog")
    public BlogResponseDto createBlog(@RequestBody BlogRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return blogService.createBlog(requestDto, userDetails.getUser());
    }

    @GetMapping("/blogs")
    public List<BlogResponseDto> getBlogs() {
        return blogService.getBlogs();
    }

    @GetMapping("/blogs/find")
    public List<BlogResponseDto> getBlogsByKeyword(@RequestParam String keyword) {
        return blogService.getBlogsByKeyword(keyword);
    }

    @PutMapping("/blog")
    public BlogResponseDto updateBlog(@RequestParam Long id, @RequestBody UpdateBlogRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return blogService.updateBlog(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/blog")
    public ResponseEntity<String> deleteBlog(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        blogService.deleteBlog(id, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body("글 삭제 성공");
    }

    @PostMapping("/blog/like")
    public String like(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return blogService.like(id, userDetails.getUser().getId());
    }

    @GetMapping("/blog/like")
    public boolean isLiked(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return blogService.isLiked(id, userDetails.getUser().getId());
    }
}
