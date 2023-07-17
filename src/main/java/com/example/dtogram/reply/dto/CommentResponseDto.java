package com.sparta.myblogbackend.dto;

import com.sparta.myblogbackend.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private Long blog_id;
    private String username;
    private String comments;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCounts;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.blog_id = comment.getBlog().getId();
        this.username = comment.getUsername();
        this.comments = comment.getComments();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCounts = comment.getCommentLikeList().size();
    }
}
