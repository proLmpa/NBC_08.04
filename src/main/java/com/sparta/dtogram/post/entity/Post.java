package com.sparta.dtogram.post.entity;

import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.UpdatePostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.reply.entity.Reply;

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
  
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;
    //@Column
    //private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Reply> replyList = new ArrayList<>();

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog", cascade = CascadeType.REMOVE)
//    private List<PostLike> blogLikeList = new ArrayList<>();

    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = user.getUsername();
        this.user = user;
        //this.likeCount = 0L;
    }

    public void update(UpdatePostRequestDto requestDto) {
        this.title =requestDto.getTitle();
        this.content = requestDto.getContent();
    }

//    public void mappingPostLike(PostLike postLike) { // 좋아요 수를 세기 위해 추가
//        this.blogLikeList.add(postLike);
//    }

//    public void updateLikeCount() { // 피드 내 좋아요 수 확인은 따로 변수를 생성하지 않고 목록의 크기로 확인
//        this.likeCount = (long)this.postLikeList.size();
//    }
//
//    public void subLikeCount(PostLike postLike) { // 좋아요 목록에서 삭제
//        this.postLikeList.remove(postLike);
//    }
}
