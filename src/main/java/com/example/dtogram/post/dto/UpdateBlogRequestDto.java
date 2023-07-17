package com.sparta.myblogbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBlogRequestDto {
    private String title;
    private String contents;
}
