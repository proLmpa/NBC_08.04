package com.sparta.dtogram.tag.controller;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.tag.dto.TagRequestDto;
import com.sparta.dtogram.tag.dto.TagResponseDto;
import com.sparta.dtogram.tag.dto.UpdateTagRequestDto;
import com.sparta.dtogram.tag.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "태그 API", description = "태그 생성/수정/삭제를 위한 API 정보를 담고 있습니다.")
public class TagController {
    private final TagService tagService;

    @Operation(summary = "태그 생성", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 TagRequesetDto를 통해 새로운 태그를 생성 후 반환합니다.")
    @PostMapping("/tag")
    public ResponseEntity<TagResponseDto> createTag(@RequestBody TagRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("태그 생성");
        TagResponseDto result = tagService.createTag(requestDto, userDetails.getUser());
        return ResponseEntity.status(200).body(result);
    }


    @Operation(summary = "태그 수정", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 UpdateTagRequesetDto를 통해 태그를 새로 수정 후 반환합니다.")
    @PutMapping("/tag")
    public ResponseEntity<TagResponseDto> updateTag(@RequestBody UpdateTagRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("태그 수정");
        TagResponseDto result = tagService.updateTag(requestDto, userDetails.getUser());
        return ResponseEntity.status(200).body(result);
    }

    @Operation(summary = "태그 삭제", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 TagRequesetDto를 통해 일치하는 기존 태그를 삭제 후 성공 메시지를 반환합니다.")
    @DeleteMapping("/tag")
    public ResponseEntity<ApiResponseDto> deleteTag(@RequestBody TagRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("태그 삭제");
        tagService.deleteTag(requestDto, userDetails.getUser());
        return ResponseEntity.status(200).body(new ApiResponseDto("태그 삭제 성공", HttpStatus.OK.value()));
    }
}
