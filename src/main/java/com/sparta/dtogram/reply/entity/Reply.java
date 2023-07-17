package com.sparta.dtogram.reply.entity;


import com.sparta.dtogram.post.entity.Timestamped;
import com.sparta.dtogram.reply.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

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
    private String comments;
    @JoinColumn(nullable = false)
    private String username;
    @Column
    private Long likeCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikeList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    public Reply(CommentRequestDto requestDto, User user, Blog blog) {
        this.comments = requestDto.getComments();
        this.username = user.getUsername();
        this.user = user;
        this.blog = blog;
        this.likeCount = 0L;
    }

    public void update(CommentRequestDto requestDto) {
        this.comments = requestDto.getComments();
    }

    public void mappingCommentLike(CommentLike commentLike) { // 좋아요 수를 세기 위해 추가
        this.commentLikeList.add(commentLike);
    }

    public void updateLikeCount() { // 피드 내 좋아요 수 확인은 따로 변수를 생성하지 않고 목록의 크기로 확인
        this.likeCount = (long)this.commentLikeList.size();
    }

    public void subLikeCount(CommentLike commentLike) { // 좋아요 목록에서 삭제
        this.commentLikeList.remove(commentLike);
    }
}
