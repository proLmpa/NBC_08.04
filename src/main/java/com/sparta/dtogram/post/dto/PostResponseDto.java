package com.sparta.dtogram.post.dto;

import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.reply.dto.ReplyResponseDto;
import com.sparta.dtogram.reply.entity.Reply;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<ReplyResponseDto> ReplyList;

    private int likeCounts;

    public PostResponseDto(Post Post) {
        this.id = Post.getId();
        this.username = Post.getUsername();
        this.title = Post.getTitle();
        this.content = Post.getContent();
        this.ReplyList = new ArrayList<>();
        for (Reply reply : Post.getReplyList()) {
            ReplyResponseDto ReplyResponseDto = new ReplyResponseDto(reply);
            this.ReplyList.add(ReplyResponseDto);
        }
        this.createdAt = Post.getCreatedAt();
        this.modifiedAt = Post.getModifiedAt();
//        this.likeCounts = Post.getPostLikeList().size();
    }
}
