package com.sparta.dtogram.reply.dto;

import com.sparta.dtogram.reply.entity.Reply;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReplyResponseDto {
    private Long id;
    private Long postId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
//    private int likeCounts;

    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.postId = reply.getPost().getId(); //todo 댓글dto에 post_id가 필요한가.
        this.username = reply.getUser().getUsername(); //todo 이부분 nickname으로 바꿀건지 체크
        this.content = reply.getContent();
        this.createdAt = reply.getCreatedAt();
        this.modifiedAt = reply.getModifiedAt();
//        this.likeCounts = reply.getReplyLikeList().size();
    }
}
