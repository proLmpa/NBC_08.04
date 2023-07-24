package com.sparta.dtogram.reply.dto;

import com.sparta.dtogram.reply.entity.Reply;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ReplyResponseDto {
    private Long id;
    private Long postId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean isLikedReply;
    private int countReplyLike;

    public ReplyResponseDto(Reply reply) {
        this.id = reply.getId();
        this.postId = reply.getPost().getId();
        this.nickname = reply.getUser().getNickname();
        this.content = reply.getContent();
        this.createdAt = reply.getCreatedAt();
        this.modifiedAt = reply.getModifiedAt();
        this.isLikedReply = reply.isLikedReply();
        this.countReplyLike = reply.getReplyLikes().size();
    }
}
