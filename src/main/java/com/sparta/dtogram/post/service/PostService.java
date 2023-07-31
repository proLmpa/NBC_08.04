package com.sparta.dtogram.post.service;

import com.sparta.dtogram.post.dto.PostRequestDto;
import com.sparta.dtogram.post.dto.PostResponseDto;
import com.sparta.dtogram.post.dto.PostsResponseDto;
import com.sparta.dtogram.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PostService {
    /*
     * 게시글 생성
     * @param requestDto 게시글 생성 요청 정보
     * @param user 사용자 권한 토큰
     * @part multipartFile 등록할 게시글 이미지 파일
     * @return 게시글 생성 결과
     */
    public PostResponseDto createPost(PostRequestDto requestDto, User user, MultipartFile multipartFile) throws IOException;

    /*
     * 단일 게시글 조회
     * @param id 조회할 게시글의 ID
     * @return 조회한 게시글 결과
     */
    public PostResponseDto getPostById(Long id);

    /*
     * 전체 게시글 조회
     * @return 전체 게시글 조회 결과
     */
    public PostsResponseDto getPosts();

    /*
     * 게시글 수정
     * @param id 수정할 게시글의 ID
     * @param requestDto 게시글 수정 요청 정보
     * @param user 사용자 권한 토큰
     * @part multipartFile 수정할 게시글 이미지 파일
     * @return 게시글 수정 결과
     */
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user, MultipartFile multipartFile) throws IOException;

    /*
     * 게시글 삭제
     * @param id 삭제할 게시글의 ID
     * @param user 사용자 권한 토큰
     */
    public void deletePost(Long id, User user);

    /*
     * 게시글 좋아요 등록
     * @param id 좋아요 등록할 게시글의 ID
     * @param user 사용자 권한 토큰
     */
    public void submitLike(Long id, User user);

    /*
     * 좋아요 기반 게시글 조회 기능
     * @param user 사용자 권한 토큰
     * @return 사용자가 좋아요 등록한 게시글 조회 결과
     */
    public PostsResponseDto getPostsByLike(User user);

    /*
     * 게시글 좋아요 해제
     * @param id 좋아요 등록할 게시글의 ID
     * @param user 사용자 권한 토큰
     */
    public void cancelLike(Long id, User user);

    /*
     * 게시글 태그 등록
     * @param postId 태그 등록할 게시글 ID
     * @param tagId 등록할 태그 ID
     * @param user 사용자 권한 토큰
     */
    public void submitTag(Long postId, Long tagId, User user);

    /*
     * 태그별 게시글 조회 기능
     * @param tagId 조회할 기준 태그 ID
     * @return 태그 기준 조회된 전체 게시글 결과
     */
    public PostsResponseDto getPostsByTag(Long tagId);

    /*
     * 게시글 태그 삭제
     * @param postId 태그 삭제할 게시글 ID
     * @param tagId 삭제할 태그 ID
     * @param user 사용자 권한 토큰
     */
    public void cancelTag(Long postId, Long tagId, User user);

    /*
     * 자신을 팔로우 중인 사용자의 전체 게시글 목록 생성
     * @param user 사용자 권한 토큰
     * @return 자신을 팔로우 중인 사용자의 전체 게시글 목록
     */
    PostsResponseDto getFollowersPosts(User user);


    /*
     * 자신이 팔로우 중인 사용자의 전체 게시글 목록 생성
     * @param user 사용자 권한 토큰
     * @return 자신이 팔로우 중인 사용자의 전체 게시글 목록
     */
    PostsResponseDto getFollowingsPosts(User user);
}