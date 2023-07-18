package com.sparta.dtogram.tag.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTagRequestDto {
    private String tag;
    private String newTag;
}
