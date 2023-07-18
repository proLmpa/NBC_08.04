package com.sparta.dtogram.post.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PostsResponseDto {
    private List<PostResponseDto> postsList;

    public PostsResponseDto(List<PostResponseDto> postList) {
        this.postsList = postList;
    }
}