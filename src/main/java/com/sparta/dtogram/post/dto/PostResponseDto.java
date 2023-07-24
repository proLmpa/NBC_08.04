package com.sparta.dtogram.post.dto;

import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.reply.dto.ReplyResponseDto;
import com.sparta.dtogram.reply.entity.Reply;
import com.sparta.dtogram.tag.dto.TagResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String imageUrl;
    private String multiMediaUrl;
    private List<TagResponseDto> tags;
    private List<ReplyResponseDto> replies;
    private boolean isLikedPost;
    private Integer countPostLike;
    //private List<PostLikeResponseDto> likes;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.nickname = post.getUser().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.imageUrl = post.getUser().getImageUrl();
        this.multiMediaUrl = post.getMultiMediaUrl();
        this.replies = new ArrayList<>();
        this.tags = post.getPostTags().stream().map(postTag -> new TagResponseDto(postTag.getTag())).toList();
        this.replies = post.getReplies().stream().map((Reply reply) -> new ReplyResponseDto(reply)).toList();
        this.countPostLike = post.getPostLikes().size();
    }

    public PostResponseDto(Post post, boolean isLikedPost) {
        this.id = post.getId();
        this.userId = post.getUser().getId();
        this.nickname = post.getUser().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.imageUrl = post.getUser().getImageUrl();
        this.multiMediaUrl = post.getMultiMediaUrl();
        this.replies = new ArrayList<>();
        this.tags = post.getPostTags().stream().map(postTag -> new TagResponseDto(postTag.getTag())).toList();
        this.replies = post.getReplies().stream().map((Reply reply) -> new ReplyResponseDto(reply)).toList();
        this.countPostLike = post.getPostLikes().size();
        this.isLikedPost = isLikedPost;
        //this.likes = post.getLikes();
    }
}
