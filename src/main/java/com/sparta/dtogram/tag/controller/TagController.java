package com.sparta.dtogram.tag.controller;

import com.sparta.dtogram.tag.dto.TagRequestDto;
import com.sparta.dtogram.tag.dto.TagResponseDto;
import com.sparta.dtogram.tag.entity.Tag;
import com.sparta.dtogram.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TagController {
    private final TagService tagService;

    @PostMapping("/tag")
    public ResponseEntity<TagResponseDto> createTag(@RequestBody TagRequestDto requestDto) {
        TagResponseDto result = tagService.createTag(requestDto);
        return ResponseEntity.status(200).body(result);
    }
}
