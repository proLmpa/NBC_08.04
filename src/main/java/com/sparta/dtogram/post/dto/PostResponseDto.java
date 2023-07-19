package com.sparta.dtogram.post.dto;

import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.reply.dto.ReplyResponseDto;
import com.sparta.dtogram.reply.entity.Reply;
import com.sparta.dtogram.tag.dto.TagResponseDto;
import com.sparta.dtogram.tag.entity.Tag;
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
    private List<ReplyResponseDto> replies;
    private Integer countPostLike;
    private List<TagResponseDto> tags;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.nickname = post.getUser().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.countPostLike = post.getPostLikes().size();
        this.replies = new ArrayList<>();
        for (Reply reply : post.getReplies()) {
            ReplyResponseDto replyResponseDto = new ReplyResponseDto(reply);
            this.replies.add(replyResponseDto);
        }
        this.tags = post.getPostTags().stream().map(postTag -> new TagResponseDto(postTag.getTag())).toList();
    }
}
