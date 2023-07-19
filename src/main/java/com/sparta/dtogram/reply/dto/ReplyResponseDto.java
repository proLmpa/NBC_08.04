package com.sparta.dtogram.reply.dto;

import com.sparta.dtogram.reply.entity.Reply;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReplyResponseDto {
    private Long id;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int countReplyLike;

    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.nickname = reply.getUser().getNickname();
        this.content = reply.getContent();
        this.createdAt = reply.getCreatedAt();
        this.modifiedAt = reply.getModifiedAt();
        this.countReplyLike = reply.getReplyLikes().size();
    }
}
