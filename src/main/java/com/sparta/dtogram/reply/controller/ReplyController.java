package com.sparta.dtogram.reply.controller;

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

    @PostMapping("/Replys")
    public ReplyResponseDto createReply(@RequestParam Long Post_id, @RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return replyService.createReply(requestDto, userDetails.getUser(), Post_id);
    }

    @PutMapping("/Reply")
    @ResponseBody
    public ReplyResponseDto updateReply(@RequestParam Long id, @RequestBody ReplyRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return replyService.updateReply(id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/Reply")
    public ResponseEntity<String> deleteReply(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        replyService.deleteReply(id, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.OK).body("댓글 삭제 성공");
    }

//    @PostMapping("/Reply/like")
//    public void like(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        replyService.like(id, userDetails.getUser().getId());
//    }

//    @GetMapping("/Reply/like")
//    public boolean isLiked(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return ReplyService.isLiked(id, userDetails.getUser().getId());
//    }
}
