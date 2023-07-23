package com.sparta.dtogram.reply.controller;

import com.sparta.dtogram.common.dto.MsgResponseDto;
import com.sparta.dtogram.common.security.UserDetailsImpl;
import com.sparta.dtogram.reply.dto.ReplyRequestDto;
import com.sparta.dtogram.reply.dto.ReplyResponseDto;
import com.sparta.dtogram.reply.service.ReplyService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.RejectedExecutionException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/reply/{id}")
    public ReplyResponseDto createReply(@PathVariable Long id, @RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("댓글 달기 시도");

        try {
            return replyService.createReply(requestDto, userDetails.getUser(), id);
        } catch (RejectedExecutionException e) {
            log.error("댓글 달기 실패", e);
            throw new RuntimeException("댓글 달기 실패", e);
        }
    }

    @GetMapping("/reply/{id}")
    public ResponseEntity<ReplyResponseDto> getReplyById(@PathVariable Long id) {
        ReplyResponseDto result = replyService.getReplyById(id);
        return ResponseEntity.ok().body(result);
    }

//    @GetMapping("/reply")
//    public ResponseEntity<RepliesResponseDto> getReplies() {
//        RepliesResponseDto result = replyService.getReplies();
//        return ResponseEntity.ok().body(result);
//    }

    @PutMapping("/reply/{id}")
    @ResponseBody
    public ReplyResponseDto updateReply(@PathVariable Long id, @RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return replyService.updateReply(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/reply/{id}")
    public ResponseEntity<String> deleteReply(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        replyService.deleteReply(id, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 성공");
    }

    @PostMapping("/reply/{id}/like")
    public ResponseEntity<MsgResponseDto> createReplyLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            replyService.createReplyLike(id, userDetails.getUser());
        } catch (DuplicateRequestException e) {
            return ResponseEntity.badRequest().body(new MsgResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MsgResponseDto("댓글 좋아요 성공", HttpStatus.ACCEPTED.value()));    }

    @DeleteMapping("/reply/{id}/like")
    public ResponseEntity<MsgResponseDto> deleteReplyLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            replyService.deleteReplyLike(id, userDetails.getUser());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MsgResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MsgResponseDto("댓글 좋아요 취소 성공", HttpStatus.ACCEPTED.value()));
    }
}
