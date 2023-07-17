package com.example.dtogram.Post.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.myblogbackend.dto.BlogRequestDto;
import com.sparta.myblogbackend.dto.UpdateBlogRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@Entity // JPA가 관리할 수 있는 Entity 클래스 지정
@Getter
@Setter
@Table(name = "blog") // 매핑할 테이블의 이름을 지정
@NoArgsConstructor
public class Blog extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 500)
    private String contents;
    @Column(nullable = false)
    private String username;
    @Column
    private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog", cascade = CascadeType.REMOVE)
    private List<BlogLike> blogLikeList = new ArrayList<>();

    public Blog(BlogRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
        this.username = user.getUsername();
        this.user = user;
        this.likeCount = 0L;
    }

    public void update(UpdateBlogRequestDto requestDto) {
        this.title =requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public void mappingBlogLike(BlogLike blogLike) { // 좋아요 수를 세기 위해 추가
        this.blogLikeList.add(blogLike);
    }

    public void updateLikeCount() { // 피드 내 좋아요 수 확인은 따로 변수를 생성하지 않고 목록의 크기로 확인
        this.likeCount = (long)this.blogLikeList.size();
    }

    public void subLikeCount(BlogLike blogLike) { // 좋아요 목록에서 삭제
        this.blogLikeList.remove(blogLike);
    }
}
