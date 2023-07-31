package com.sparta.dtogram.post.repository;

import com.sparta.dtogram.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostCustomRepository {
    /*
     * 자신을 팔로우 중인 사용자의 게시글만 모아보기
     * @param userId 자신의 사용자 ID
     * @return 자신을 팔로우 중인 사용자의 게시글 조회 결과
     */
    Page<Post> getFollowersPostsByUserId(Long userId, Pageable pageable);

    /*
     * 자신이 팔로우 중인 사용자의 게시글만 모아보기
     * @param userId 자신의 사용자 ID
     * @return 자신이 팔로우 중인 사용자의 게시글 조회 결과
     */
    Page<Post> getFollowingsPostsByUserId(Long userId, Pageable pageable);

    /*
     * 태그 기준 게시글 조회하기
     * @param tagId 기준이 될 태그 ID
     * @return 기준 태그 ID를 포함한 게시글 조회 결과
     */
    List<Post> getPostsByTagId(Long tagId);

    /*
     * 자신이 좋아요를 누른 게시글 조회하기
     * @param userId 자신의 사용자 ID
     * @return 자신이 좋아요를 등록한 게시글 조회 결과
     */
    List<Post> getLikedPostsByUserId(Long userId);
}
