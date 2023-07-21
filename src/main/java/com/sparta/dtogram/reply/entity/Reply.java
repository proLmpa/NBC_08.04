package com.sparta.dtogram.reply.entity;

import com.sparta.dtogram.common.entity.Timestamped;
import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.reply.dto.ReplyRequestDto;
import com.sparta.dtogram.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reply")
public class Reply extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reply", cascade = CascadeType.REMOVE)
    private List<ReplyLike> replyLikes = new ArrayList<>();


    public Reply(ReplyRequestDto requestDto, User user, Post post) {
        this.content = requestDto.getContent();
        this.user = user;
        this.post = post;
    }

    public void update(ReplyRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void registerReplyLike(ReplyLike replyLike) {
        this.replyLikes.add(replyLike);
    }

    public void cancelReplyLike(ReplyLike replyLike){
        this.replyLikes.remove(replyLike);
    }
}