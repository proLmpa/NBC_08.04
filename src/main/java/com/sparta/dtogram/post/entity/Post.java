package com.sparta.dtogram.post.entity;

import com.sparta.dtogram.common.entity.Timestamped;
import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.UpdatePostRequestDto;
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
@Table(name = "Post") // 매핑할 테이블의 이름을 지정
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
    //@Column
    //private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "Post", cascade = CascadeType.ALL)
    private List<Reply> replyList = new ArrayList<>();

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "Post", cascade = CascadeType.REMOVE)
//    private List<PostLike> PostLikeList = new ArrayList<>();

    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = user.getUsername();
        this.user = user;
        //this.likeCount = 0L;
    }

    public void update(UpdatePostRequestDto requestDto) {
        this.title =requestDto.getTitle();
        this.content = requestDto.getContents();
    }

//    public void mappingPostLike(PostLike PostLike) { // 좋아요 수를 세기 위해 추가
//        this.PostLikeList.add(PostLike);
//    }
//
//    public void updateLikeCount() { // 피드 내 좋아요 수 확인은 따로 변수를 생성하지 않고 목록의 크기로 확인
//        this.likeCount = (long)this.PostLikeList.size();
//    }
//
//    public void subLikeCount(PostLike PostLike) { // 좋아요 목록에서 삭제
//        this.PostLikeList.remove(PostLike);
//    }
}
