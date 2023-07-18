package com.sparta.dtogram.reply.service;

import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.post.repository.PostRepository;
import com.sparta.dtogram.reply.dto.ReplyRequestDto;
import com.sparta.dtogram.reply.dto.ReplyResponseDto;
import com.sparta.dtogram.reply.entity.Reply;
import com.sparta.dtogram.reply.entity.ReplyLike;
import com.sparta.dtogram.reply.repository.ReplyLikeRepository;
import com.sparta.dtogram.reply.repository.ReplyRepository;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReplyLikeRepository replyLikeRepository;


    public ReplyResponseDto createReply(ReplyRequestDto requestDto, User user, Long postId) {
            Post Post = postRepository.findById(postId).orElseThrow(() ->
                    new IllegalArgumentException("해당 글을 찾을 수 없습니다.")
            );
            Reply reply = replyRepository.save(new Reply(requestDto, user, Post));

            return new ReplyResponseDto(reply);
    }

    @Transactional
    public ReplyResponseDto updateReply(Long id, ReplyRequestDto requestDto, User user) {
        Reply reply = findReply(id);
        if (reply.getUser().getUsername().equals(user.getUsername())) {
            reply.update(requestDto);
        } else {
            throw new RuntimeException("작성자만 삭제/수정할 수 있습니다.");
        }

        return new ReplyResponseDto(reply);
    }

    public void deleteReply(Long id, User user) {
        Reply Reply = findReply(id);
        if (Reply.getUser().getUsername().equals(user.getUsername())) {
            replyRepository.delete(Reply);
        } else {
            throw new RuntimeException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    @Transactional
    public void likeReply(Long id, User user) {
        Reply reply = findReply(id);
        Optional<ReplyLike> replyLike = replyLikeRepository.findByUserAndReply(user, reply);
        if (replyLike.isPresent()) {
            replyLikeRepository.delete(replyLike.get());
        } else {
            throw new IllegalArgumentException("댓글 좋아요 중복선택");
        }
    }

    @Transactional
    public void dislikeReply(Long id, User user) {
        Reply reply = findReply(id);
        Optional<ReplyLike> replyLike = replyLikeRepository.findByUserAndReply(user, reply);
        if (replyLike.isPresent()) {
            replyLikeRepository.delete(replyLike.get());
        } else {
            throw new IllegalArgumentException("존재하지 않는 좋아요");
        }
    }

    private Reply findReply(Long id) {
        return replyRepository.findById(id).orElseThrow(() -> // null 체크
                new IllegalArgumentException("존재하지 않는 게시글")
        );
    }

//    private User findUser(Long id) {
//        return userRepository.findById(id).orElseThrow(() ->
//                new IllegalArgumentException("존재하지 않는 유저")
//        );
//    }
}
