package com.sparta.dtogram.user.entity;

import com.sparta.dtogram.follow.entity.Follow;
import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.post.entity.PostLike;
import com.sparta.dtogram.profile.entity.PasswordHistory;
import com.sparta.dtogram.reply.entity.Reply;
import com.sparta.dtogram.reply.entity.ReplyLike;
import com.sparta.dtogram.profile.dto.ProfileRequestDto;
import com.sparta.dtogram.tag.entity.Tag;
import com.sparta.dtogram.user.dto.SignupRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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

    @Column
    private String imageUrl; // 프로필 이미지를 저장하기 위한 변수

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    private Long kakaoId;

    private String naverId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<ReplyLike> replyLikes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="following", orphanRemoval = true)
    private List<Follow> followings = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "follower", orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<PasswordHistory> passwordHistories = new ArrayList<>();

    public User(SignupRequestDto requestDto, String password, UserRoleEnum role) {
        this.username = requestDto.getUsername();
        this.nickname = requestDto.getNickname();
        this.password = password;
        this.email = requestDto.getEmail();
        this.role = role;
    }

    //todo 추후 다른 소셜 로그인과 섞일 염려가 있음
    //KAKAO User생성
    public User(String username, String password, String email, UserRoleEnum role, Long kakaoId) {
        this.username = "kakao" + username;
        this.nickname = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    //NAVER User생성
    public User(String username, String nickname, String password, String email, UserRoleEnum role, String naverId) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.role = role;
        this.naverId = naverId;
    }

    public void updateProfile(ProfileRequestDto requestDto) {
        this.nickname = requestDto.getNickname()==null ? this.nickname : requestDto.getNickname();
        this.introduction = requestDto.getIntroduction()==null ? this.introduction : requestDto.getIntroduction();
    }

    public void updateProfileImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    public User naverIdUpdate(String naverId) {
        this.naverId = naverId;
        return this;
    }

    public void updateRole(UserRoleEnum role) {
        this.role = role;
    }
}