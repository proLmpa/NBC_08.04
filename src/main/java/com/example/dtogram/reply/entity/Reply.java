package com.example.dtogram.reply.entity;

import com.example.dtogram.post.entity.Post;
import com.example.dtogram.reply.dto.ReplyRequestDto;
import com.example.dtogram.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "reply")
public class Reply extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;
    @JoinColumn(nullable = false)
    private String username;
    @Column
    private Long likeCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reply", cascade = CascadeType.REMOVE)
    private List<ReplyLike> replyLikeList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Reply(ReplyRequestDto requestDto, User user, Post post) {
        this.content = requestDto.getContent();
        this.username = user.getUsername();
        this.user = user;
        this.post = post;
        this.likeCount = 0L;
    }

    public void update(ReplyRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void mappingReplyLike(ReplyLike replyLike) { // 좋아요 수를 세기 위해 추가
        this.replyLikeList.add(replyLike);
    }

    public void updateLikeCount() { // 피드 내 좋아요 수 확인은 따로 변수를 생성하지 않고 목록의 크기로 확인
        this.likeCount = (long)this.replyLikeList.size();
    }

    public void subLikeCount(ReplyLike replyLike) { // 좋아요 목록에서 삭제
        this.replyLikeList.remove(replyLike);
    }
}
