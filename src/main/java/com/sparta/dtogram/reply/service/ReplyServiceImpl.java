package com.sparta.dtogram.reply.service;

import com.sparta.dtogram.common.error.DtogramErrorCode;
import com.sparta.dtogram.common.exception.DtogramException;
import com.sparta.dtogram.post.entity.Post;
import com.sparta.dtogram.post.repository.PostRepository;
import com.sparta.dtogram.reply.dto.ReplyRequestDto;
import com.sparta.dtogram.reply.dto.ReplyResponseDto;
import com.sparta.dtogram.reply.entity.Reply;
import com.sparta.dtogram.reply.entity.ReplyLike;
import com.sparta.dtogram.reply.repository.ReplyLikeRepository;
import com.sparta.dtogram.reply.repository.ReplyRepository;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{
    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final ReplyLikeRepository replyLikeRepository;

    // 댓글 생성
    @Override
    @Transactional
    public ReplyResponseDto createReply(ReplyRequestDto requestDto, User user, Long postId) {
        Post post = findPost(postId);
        Reply reply = replyRepository.save(new Reply(requestDto, user, post));

        return new ReplyResponseDto(reply);
    }

    // 단일 댓글 조회
    @Override
    @Transactional(readOnly = true)
    public ReplyResponseDto getReplyById(Long id) {
        Reply reply = findReply(id);
        return new ReplyResponseDto(reply);
    }

    // 댓글 수정
    @Override
    @Transactional
    public ReplyResponseDto updateReply(Long id, ReplyRequestDto requestDto, User user) {
        Reply reply = findReply(id);

        if (matchUser(reply, user)) {
            reply.updateReply(requestDto);
        } else {
            throw new DtogramException(DtogramErrorCode.UNAUTHORIZED_USER, null);
        }

        return new ReplyResponseDto(reply);
    }

    // 댓글 삭제
    @Override
    @Transactional
    public void deleteReply(Long id, User user) {
        Reply reply = findReply(id);

        if (matchUser(reply, user)) {
            replyRepository.delete(reply);
        } else {
            throw new DtogramException(DtogramErrorCode.UNAUTHORIZED_USER, null);
        }
    }

    // 댓글 좋아요 등록
    @Override
    @Transactional
    public void submitLike(Long id, User user) {
        Reply reply = findReply(id);
        ReplyLike replyLike = findReplyLike(reply, user);

        if(replyLike == null) {
            replyLike = new ReplyLike(user, reply);
            reply.registerReplyLike(replyLike);
            replyLikeRepository.save(replyLike);
        } else {
            throw new DtogramException(DtogramErrorCode.LIKE_ALREADY_EXISTS, null);
        }
    }

    // 댓글 좋아요 삭제
    @Override
    @Transactional
    public void cancelLike(Long id, User user) {
        Reply reply = findReply(id);
        ReplyLike replyLike = findReplyLike(reply, user);

        if(replyLike != null) {
            reply.cancelReplyLike(replyLike);
            replyLikeRepository.delete(replyLike);
        } else {
            throw new DtogramException(DtogramErrorCode.LIKE_NOT_FOUND, null);
        }
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new DtogramException(DtogramErrorCode.POST_NOT_FOUND, null)
        );
    }

    private Reply findReply(Long id) {
        return replyRepository.findById(id).orElseThrow(() -> // null 체크
                new DtogramException(DtogramErrorCode.REPLY_NOT_FOUND, null)
        );
    }

    private ReplyLike findReplyLike(Reply reply, User user) {
        return replyLikeRepository.findByUserAndReply(user, reply).orElse(null);
    }

    private boolean matchUser(Reply reply, User user) {
        return reply.getUser().getId().equals(user.getId()) || user.getRole().equals(UserRoleEnum.ADMIN);
    }
}
