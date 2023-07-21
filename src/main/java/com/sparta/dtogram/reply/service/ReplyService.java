package com.sparta.dtogram.reply.service;

import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.post.repository.PostRepository;
import com.sparta.dtogram.reply.dto.RepliesResponseDto;
import com.sparta.dtogram.reply.dto.ReplyRequestDto;
import com.sparta.dtogram.reply.dto.ReplyResponseDto;
import com.sparta.dtogram.reply.entity.Reply;
import com.sparta.dtogram.reply.entity.ReplyLike;
import com.sparta.dtogram.reply.repository.ReplyLikeRepository;
import com.sparta.dtogram.reply.repository.ReplyRepository;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.repository.UserRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final ReplyLikeRepository replyLikeRepository;

    @Transactional
    public ReplyResponseDto createReply(ReplyRequestDto requestDto, User user, Long postId) {
            Post Post = postRepository.findById(postId).orElseThrow(() ->
                    new IllegalArgumentException("Exception ! 존재하지 않는 게시글에 댓글 달기 시도 감지")
            );
            Reply reply = replyRepository.save(new Reply(requestDto, user, Post));

            return new ReplyResponseDto(reply);
    }

    @Transactional(readOnly = true)
    public ReplyResponseDto getReplyById(Long id) {
        Reply reply = findReply(id);

        return new ReplyResponseDto(reply);
    }

//    public RepliesResponseDto getReplies() {
//        return replyRepository.findAll();
//    }

    @Transactional
    public ReplyResponseDto updateReply(Long id, ReplyRequestDto requestDto, User user) {
        Reply reply = findReply(id);
        if (reply.getUser().getUsername().equals(user.getUsername())) {
            reply.update(requestDto);
        } else {
            throw new RuntimeException("Exception ! 작성자가 아닌 게시글 수정 시도 감지");
        }

        return new ReplyResponseDto(reply);
    }

    @Transactional
    public void deleteReply(Long id, User user) {
        Reply Reply = findReply(id);
        if (Reply.getUser().getUsername().equals(user.getUsername())) {
            replyRepository.delete(Reply);
        } else {
            throw new RuntimeException("Exception ! 작성자가 아닌 게시글 삭제 시도 감지");
        }
    }

    @Transactional
    public void createReplyLike(Long id, User user) {
        log.info("댓글 좋아요 누르기 시도");
        Reply reply = findReply(id);
        ReplyLike replyLike = new ReplyLike(user, reply);

        if(!replyLikeRepository.findByUserAndReply(user, reply).isPresent()) {
            log.info("댓글 좋아요 누르기 성공");
            reply.registerReplyLike(replyLike);
            replyLikeRepository.save(replyLike);
        } else {
            replyLikeRepository.delete(replyLike);
        }
    }

    @Transactional
    public void deleteReplyLike(Long id, User user) {
        Reply reply = findReply(id);
        Optional<ReplyLike> replyLike = replyLikeRepository.findByUserAndReply(user, reply);

        if (replyLike.isPresent()) {
            reply.cancelReplyLike(replyLike.get());
        } else {
            throw new IllegalArgumentException("Exception ! 존재하지 않는 게시글에 대한 좋아요 누르기 시도 감지");
        }
    }

    private Reply findReply(Long id) {
        return replyRepository.findById(id).orElseThrow(() -> // null 체크
                new IllegalArgumentException("Exception ! 존재하지 않는 댓글 찾기 시도 감지")
        );
    }


}
