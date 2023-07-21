package com.sparta.dtogram.follow.entity;

import com.sparta.dtogram.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "follow") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    public Follow(User following, User follower) {
        this.following = following;
        this.follower = follower;
        following.getFollowers().add(this);
        follower.getFollowings().add(this);
    }

    public void cancelFollow(User following, User follower) {
        following.getFollowers().remove(this);
        follower.getFollowings().remove(this);
    }
}

