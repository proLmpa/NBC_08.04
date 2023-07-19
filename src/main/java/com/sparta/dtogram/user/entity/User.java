package com.sparta.dtogram.user.entity;

import com.sparta.dtogram.post.entity.PostLike;
import com.sparta.dtogram.reply.entity.ReplyLike;
import com.sparta.dtogram.user.dto.ProfileRequestDto;
import com.sparta.dtogram.user.dto.SignupRequestDto;
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
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String introduction;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    private Long kakaoId;


  
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

    public User(String username, String password, String email, UserRoleEnum role, Long kakaoId) {
        this.username = "kakao"+username;
        this.nickname = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId =kakaoId;
    }

    public User(String username, String password, String email, UserRoleEnum role, Long kakaoId) {
        this.username = "kakao"+username;
        this.nickname = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId =kakaoId;
    }

    public void updateProfile(ProfileRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.introduction = requestDto.getIntroduction();
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }
}