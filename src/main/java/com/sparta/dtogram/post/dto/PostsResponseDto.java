package com.sparta.dtogram.post.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostsResponseDto {
    private List<PostResponseDto> posts;

    public PostsResponseDto(List<PostResponseDto> posts) {
        this.posts = posts;
    }
}