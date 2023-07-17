package com.example.dtogram.reply.dto;

import com.example.dtogram.reply.entity.Reply;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReplyResponseDto {
    private Long id;
    private Long post_id;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCounts;

    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.post_id = reply.getPost().getId();
        this.username = reply.getUsername();
        this.content = reply.getContent();
//        this.createdAt = reply.getCreatedAt();
//        this.modifiedAt = reply.getModifiedAt();
        this.likeCounts = reply.getReplyLikeList().size();
    }
}
