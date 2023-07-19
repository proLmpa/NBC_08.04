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

    @Column
    private Long likeCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reply", cascade = CascadeType.REMOVE)
    private List<ReplyLike> ReplyLikeList = new ArrayList<>();


    public Reply(ReplyRequestDto requestDto, User user, Post post) {
        this.content = requestDto.getContent();
        this.user = user;
        this.post = post;
        this.likeCount = 0L;
    }

    public void update(ReplyRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

//    public void mappingReplyLike(ReplyLike ReplyLike) { // 좋아요 수를 세기 위해 추가
//        this.ReplyLikeList.add(ReplyLike);
//    }
//
//    public void updateLikeCount() { // 피드 내 좋아요 수 확인은 따로 변수를 생성하지 않고 목록의 크기로 확인
//        this.likeCount = (long)this.ReplyLikeList.size();
//    }
//
//    public void subLikeCount(ReplyLike ReplyLike) { // 좋아요 목록에서 삭제
//        this.ReplyLikeList.remove(ReplyLike);
//    }
}
