package com.sparta.dtogram.user.entity;

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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;


  
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
//    private List<PostLike> PostLikeList = new ArrayList<>();
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
//    private List<ReplyLike> ReplyLikeList = new ArrayList<>();

  

    public User(String username, String nickname, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
    }

//    public void mappingPostLike(PostLike PostLike) { // 유저가 해당 좋아요를 눌렀는지 확인
//        this.PostLikeList.add(PostLike);
//    }
//    public void mappingReplyLike(ReplyLike ReplyLike) { // 유저가 해당 좋아요를 눌렀는지 확인
//        this.ReplyLikeList.add(ReplyLike);
//    }
}