package com.sparta.dtogram.follow.service;

import com.sparta.dtogram.user.entity.User;

import java.util.List;

public interface FollowService {
    /*
     * 사용자 팔로우
     * @param followerId 팔로우 주체 사용자의 ID
     * @param followingId 팔로우 대상 사용자의 ID
     * @return 팔로우 성공 메시지 반환
    */
    public String followUser(Long followerId, Long followingId) throws IllegalAccessException;

    /*
     * 사용자 언팔로우
     * @param followerId 언팔로우 주체 사용자의 ID
     * @param followingId 언팔로우 대상 사용자의 ID
     * @return 언팔로우 성공 메시지 반환
     */
    public String unfollowUser(Long followerId, Long followingId);

    /*
     * 자신을 팔로우 중인 사용자들만 조회하기
     * @param user 사용자 권한 토큰
     * @return 팔로워 리스트 결과
     */
    public List<User> getFollowerList(User user);

    /*
     * 자신이 팔로우 중인 사용자들만 조회하기
     * @param user 사용자 권한 토큰
     * @return 팔로윙 리스트 결과
     */
    public List<User> getFollowingList(User user);
}