package com.sparta.dtogram.reply.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RepliesResponseDto {
    private Long postId;
    private List<ReplyResponseDto> replies;

    public RepliesResponseDto() {
        this.replies = getReplies();
    }
}
