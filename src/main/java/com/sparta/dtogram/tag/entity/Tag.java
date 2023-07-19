package com.sparta.dtogram.tag.entity;

import com.sparta.dtogram.tag.dto.TagRequestDto;
import com.sparta.dtogram.tag.dto.UpdateTagRequestDto;
import com.sparta.dtogram.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Tag(TagRequestDto requestDto, User user) {
        this.tag = requestDto.getTag();
        this.user = user;
    }

    public void update(UpdateTagRequestDto requestDto) {
        this.tag = requestDto.getNewTag();
    }
}
