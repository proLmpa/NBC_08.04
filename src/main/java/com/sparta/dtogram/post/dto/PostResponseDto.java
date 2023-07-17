package com.sparta.dtogram.post.dto;

import com.sparta.dtogram.post.entity.Post;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    //private List<ReplyResponseDto> replyList;
    //private int likeCounts;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.nickname = post.getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
//        this.replyList = new ArrayList<>();
//        for (Reply reply : post.getreplies()) {
//            ReplyResponseDto replyResponseDto = new ReplyResponseDto(reply);
//            this.replyList.add(replyResponseDto);
//        }
        //this.likeCounts = post.getPostLikeList().size();
    }
}
