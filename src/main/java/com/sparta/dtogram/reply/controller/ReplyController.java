package com.sparta.dtogram.reply.controller;

import com.sparta.dtogram.common.dto.ApiResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.reply.dto.ReplyRequestDto;
import com.sparta.dtogram.reply.dto.ReplyResponseDto;
import com.sparta.dtogram.reply.service.ReplyService;
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
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/reply/{id}")
    public ResponseEntity<ReplyResponseDto> createReply(@PathVariable Long id, @RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("댓글 생성");
        ReplyResponseDto result = replyService.createReply(requestDto, userDetails.getUser(), id);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/reply/{id}")
    public ResponseEntity<ReplyResponseDto> getReplyById(@PathVariable Long id) {
        log.info("댓글 조회");
        ReplyResponseDto result = replyService.getReplyById(id);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/reply/{id}")
    public ResponseEntity<ReplyResponseDto> updateReply(@PathVariable Long id, @RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("댓글 수정");
        ReplyResponseDto result = replyService.updateReply(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/reply/{id}")
    public ResponseEntity<ApiResponseDto> deleteReply(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("댓글 삭제");
        replyService.deleteReply(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("댓글 삭제 성공", HttpStatus.OK.value()));
    }

    @PostMapping("/reply/{id}/like")
    public ResponseEntity<ApiResponseDto> submitLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("댓글 좋아요 등록하기");
        replyService.submitLike(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("댓글 좋아요 등록 성공", HttpStatus.OK.value()));
    }

    @DeleteMapping("/reply/{id}/like")
    public ResponseEntity<ApiResponseDto> cancelLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("댓글 좋아요 취소");
        replyService.cancelLike(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("댓글 좋아요 취소 성공", HttpStatus.OK.value()));
    }
}
