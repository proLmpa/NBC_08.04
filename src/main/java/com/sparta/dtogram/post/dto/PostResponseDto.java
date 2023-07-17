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
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<ReplyResponseDto> replyList = new ArrayList<>();
    //private int likeCounts;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.nickname = post.getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();

        for(Reply reply : post.getReplyList()){
            this.replyList.add(new ReplyResponseDto(reply));
        }
        //this.likeCounts = post.getPostLikeList().size();
    }
}
