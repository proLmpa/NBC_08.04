package com.sparta.dtogram.post.entity;

import com.sparta.dtogram.common.entity.Timestamped;
import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.reply.entity.Reply;
import com.sparta.dtogram.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "post")
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @Column
    private String multiMediaUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private boolean isLikedPost;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostLike> postLikes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<PostTag> postTags = new ArrayList<>();

    public Post(PostRequestDto requestDto, User user, String multiMediaUrl) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
        this.multiMediaUrl = multiMediaUrl;
    }

    public Post(Long postId, PostRequestDto requestDto, User user, String multiMediaUrl) {
        this.id = postId;
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
        this.multiMediaUrl = multiMediaUrl;
    }

    public void updatePost(PostRequestDto requestDto, String multiMediaUrl) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.multiMediaUrl = multiMediaUrl;
    }
}
