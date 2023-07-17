package com.sparta.dtogram.post.dto;

import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.reply.dto.ReplyResponseDto;
import com.sparta.dtogram.reply.entity.Reply;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String username;
    private String title;
    private String contents;
    private List<ReplyResponseDto> ReplyList;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likeCounts;

    public PostResponseDto(Post Post) {
        this.id = Post.getId();
        this.username = Post.getUsername();
        this.title = Post.getTitle();
        this.contents = Post.getContents();
        this.ReplyList = new ArrayList<>();
        for (Reply reply : Post.getReplys()) {
            ReplyResponseDto ReplyResponseDto = new ReplyResponseDto(reply);
            this.ReplyList.add(ReplyResponseDto);
        }
        this.createdAt = Post.getCreatedAt();
        this.modifiedAt = Post.getModifiedAt();
//        this.likeCounts = Post.getPostLikeList().size();
    }
}
