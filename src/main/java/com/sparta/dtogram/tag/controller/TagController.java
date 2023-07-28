package com.sparta.dtogram.tag.controller;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.tag.dto.TagRequestDto;
import com.sparta.dtogram.tag.dto.TagResponseDto;
import com.sparta.dtogram.tag.dto.UpdateTagRequestDto;
import com.sparta.dtogram.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TagController {
    private final TagService tagService;

    @PostMapping("/tag")
    public ResponseEntity<TagResponseDto> createTag(@RequestBody TagRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("태그 생성");
        TagResponseDto result = tagService.createTag(requestDto, userDetails.getUser());
        return ResponseEntity.status(200).body(result);
    }

    @PutMapping("/tag")
    public ResponseEntity<TagResponseDto> updateTag(@RequestBody UpdateTagRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("태그 수정");
        TagResponseDto result = tagService.updateTag(requestDto, userDetails.getUser());
        return ResponseEntity.status(200).body(result);
    }

    @DeleteMapping("/tag")
    public ResponseEntity<ApiResponseDto> deleteTag(@RequestBody TagRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("태그 삭제");
        tagService.deleteTag(requestDto, userDetails.getUser());
        return ResponseEntity.status(200).body(new ApiResponseDto("태그 삭제 성공", HttpStatus.OK.value()));
    }
}
