package com.example.dtogram.reply.service;

import com.example.dtogram.post.entity.Post;
import com.example.dtogram.post.repository.PostRepository;
import com.example.dtogram.reply.dto.ReplyRequestDto;
import com.example.dtogram.reply.dto.ReplyResponseDto;
import com.example.dtogram.reply.entity.Reply;
import com.example.dtogram.reply.repository.ReplyRepository;
import com.example.dtogram.user.entity.User;
import com.example.dtogram.user.repository.UserRepository;
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
//    private final ReplyLikeRepository replyLikeRepository;


    public ReplyResponseDto createReply(ReplyRequestDto requestDto, User user, Long post_id) {
            Post post = postRepository.findById(post_id).orElseThrow(() ->
                    new IllegalArgumentException("해당 글을 찾을 수 없습니다.")
            );
            Reply reply = replyRepository.save(new Reply(requestDto, user, post));

            return new ReplyResponseDto(reply);
    }

    @Transactional
    public ReplyResponseDto updateReply(Long id, ReplyRequestDto requestDto, User user) {
        Reply reply = findReply(id);
        if (reply.getUsername().equals(user.getUsername())) {
            reply.update(requestDto);
        } else {
            throw new RuntimeException("작성자만 삭제/수정할 수 있습니다.");
        }

        return new ReplyResponseDto(reply);
    }

    public void deleteReply(Long id, User user) {
        Reply reply = findReply(id);
        if (reply.getUsername().equals(user.getUsername())) {
            replyRepository.delete(reply);
        } else {
            throw new RuntimeException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    private Reply findReply(Long id) {
        return replyRepository.findById(id).orElseThrow(() -> // null 체크
                new IllegalArgumentException("선택한 글은 존재하지 않습니다.")
        );
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 유저입니다.")
        );
    }
//
//    public void like(Long replyId, Long userId) {
//        Reply reply = findReply(replyId);
//        User user = findUser(userId);
//        Optional<ReplyLike> isLike = replyLikeRepository.findByUserAndReply(user, reply);
//
//        isLike.ifPresentOrElse(
//                like -> {
//                    replyLikeRepository.delete(like);
//                    reply.subLikeCount(like);
//                    reply.updateLikeCount();
//                },
//                () -> {
//                    ReplyLike replyLike = new ReplyLike(user, reply);
//
//                    replyLike.mappingReply(reply);
//                    replyLike.mappingUser(user);
//                    reply.updateLikeCount();
//
//                    replyLikeRepository.save(replyLike);
//                }
//        );
//    }
//
//    public boolean isLiked(Long replyId, Long userId) {
//        Reply reply = findReply(replyId);
//        User user = userRepository.findById(userId).orElse(new User());
//        Optional<ReplyLike> isLike = replyLikeRepository.findByUserAndReply(user, reply);
//        boolean isLiked = ReplyLike.isLikedReply(isLike);
//        return isLiked;
//    }
}
