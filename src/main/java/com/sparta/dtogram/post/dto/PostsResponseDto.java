package com.sparta.dtogram.post.dto;

import com.sparta.dtogram.reply.dto.RepliesResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostsResponseDto {
    private List<PostResponseDto> posts;
    private RepliesResponseDto replies;

    public PostsResponseDto(List<PostResponseDto> posts) {
        this.posts = posts;
        replies = new RepliesResponseDto();
    }
}