package com.sparta.dtogram.reply.entity;

import com.sparta.dtogram.common.entity.Timestamped;
import com.sparta.dtogram.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "reply_like")
public class ReplyLike extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Reply reply;

    public ReplyLike(User user, Reply reply) {
        this.user = user;
        this.reply = reply;
    }
}