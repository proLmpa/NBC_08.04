package com.sparta.dtogram.reply.service;

import com.sparta.dtogram.reply.dto.ReplyRequestDto;
import com.sparta.dtogram.reply.dto.ReplyResponseDto;
import com.sparta.dtogram.user.entity.User;

public interface ReplyService {
    /*
     * 댓글 생성
     * @param requestDto 댓글 생성 요청 정보
     * @param user 사용자 권한 토큰
     * @param postId 댓글을 작성할 게시글 ID
     * @return 댓글 생성 결과
     */
    public ReplyResponseDto createReply(ReplyRequestDto requestDto, User user, Long postId);

    /*
     * 단일 댓글 조회
     * @param id 조회할 댓글의 ID
     * @return 조회한 댓글 결과
     */
    public ReplyResponseDto getReplyById(Long id);

    /*
     * 댓글 수정
     * @param id 수정할 댓글의 ID
     * @param requestDto 댓글 수정 요청 정보
     * @param user 사용자 권한 토큰
     * @return 댓글 수정 결과
     */
    public ReplyResponseDto updateReply(Long id, ReplyRequestDto requestDto, User user);

    /*
     * 댓글 삭제
     * @param id 삭제할 댓글의 ID
     * @param user 사용자 권한 토큰
     */
    public void deleteReply(Long id, User user);

    /*
     * 댓글 좋아요 등록
     * @param id 좋아요 등록할 댓글의 ID
     * @param user 사용자 권한 토큰
     */
    public void submitLike(Long id, User user);

    /*
     * 댓글 좋아요 해제
     * @param id 좋아요 해제할 댓글의 ID
     * @param user 사용자 권한 토큰
     */
    public void cancelLike(Long id, User user);
}
