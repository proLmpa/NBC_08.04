package com.sparta.dtogram.reply.controller;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.reply.dto.ReplyRequestDto;
import com.sparta.dtogram.reply.dto.ReplyResponseDto;
import com.sparta.dtogram.reply.service.ReplyService;
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
@Tag(name = "댓글 API", description = "댓글 생성/조회/수정/삭제 및 댓글 좋아요 등록/해제를 위한 API 정보를 담고 있습니다.")
public class ReplyController {

    private final ReplyService replyService;

    @Operation(summary = "댓글 생성", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 ReplyRequestDto를 통해 댓글을 생성 후 등록을 원하는 게시글에 댓글을 추가합니다.")
    @PostMapping("/reply/{id}")
    public ResponseEntity<ReplyResponseDto> createReply(@PathVariable Long id, @RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("댓글 생성");
        ReplyResponseDto result = replyService.createReply(requestDto, userDetails.getUser(), id);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "단일 댓글 조회", description = "댓글 ID를 통해 댓글의 존재 유무 확인 후 존재 시 선택된 댓글을 반환합니다.")
    @GetMapping("/reply/{id}")
    public ResponseEntity<ReplyResponseDto> getReplyById(@PathVariable Long id) {
        log.info("댓글 조회");
        ReplyResponseDto result = replyService.getReplyById(id);
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "댓글 수정", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 ReplyRequestDto를 통해 기존 댓글을 수정합니다.")
    @PutMapping("/reply/{id}")
    public ResponseEntity<ReplyResponseDto> updateReply(@PathVariable Long id, @RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("댓글 수정");
        ReplyResponseDto result = replyService.updateReply(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "댓글 삭제", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 댓글 ID를 통해 존재 유무 확인 후 존재 시 선택된 댓글을 삭제합니다.")
    @DeleteMapping("/reply/{id}")
    public ResponseEntity<ApiResponseDto> deleteReply(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("댓글 삭제");
        replyService.deleteReply(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("댓글 삭제 성공", HttpStatus.OK.value()));
    }

    @Operation(summary = "댓글 좋아요 등록", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 댓글 ID를 통해 존재 유무 확인 후 존재 시 해당 댓글에 좋아요를 등록합니다.")
    @PostMapping("/reply/{id}/like")
    public ResponseEntity<ApiResponseDto> submitLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("댓글 좋아요 등록하기");
        replyService.submitLike(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("댓글 좋아요 등록 성공", HttpStatus.OK.value()));
    }

    @Operation(summary = "댓글 좋아요 해제", description = "전달된 Bearer 토큰을 통해 본인 확인을 마친 후 댓글 ID를 통해 존재 유무 확인 후 존재 시 해당 댓글에 등록됐던 좋아요를 해제합니다.")
    @DeleteMapping("/reply/{id}/like")
    public ResponseEntity<ApiResponseDto> cancelLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("댓글 좋아요 취소");
        replyService.cancelLike(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("댓글 좋아요 취소 성공", HttpStatus.OK.value()));
    }
}
