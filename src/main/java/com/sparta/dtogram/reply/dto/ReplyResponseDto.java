package com.sparta.dtogram.reply.dto;

import com.sparta.dtogram.reply.entity.Reply;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReplyResponseDto {
    private Long id;
    private Long Post_id;
    private String username;
    private String Replys;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCounts;

    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.Post_id = reply.getPost().getId();
        this.username = reply.getUsername();
        this.Replys = reply.getReplys();
        this.createdAt = reply.getCreatedAt();
        this.modifiedAt = reply.getModifiedAt();
//        this.likeCounts = reply.getReplyLikeList().size();
    }
}
