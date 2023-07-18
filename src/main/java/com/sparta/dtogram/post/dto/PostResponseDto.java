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
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<ReplyResponseDto> ReplyList;

    private int likeCounts;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.nickname = post.getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.ReplyList = new ArrayList<>();
        for (Reply reply : post.getReplyList()) {
            ReplyResponseDto replyResponseDto = new ReplyResponseDto(reply);
            this.ReplyList.add(replyResponseDto);
        }
        //this.likeCounts = post.getPostLikeList().size();
    }
}
