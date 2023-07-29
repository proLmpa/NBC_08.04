package com.sparta.dtogram.tag.service;

import com.sparta.dtogram.tag.dto.TagRequestDto;
import com.sparta.dtogram.tag.dto.TagResponseDto;
import com.sparta.dtogram.tag.dto.UpdateTagRequestDto;
import com.sparta.dtogram.user.entity.User;

public interface TagService {
    /*
     * 태그 생성
     * @param requestDto 태그 생성 요청 정보
     * @param user 사용자 권한 토큰
     * @return responseDto 태그 생성 결과
    * */
    public TagResponseDto createTag(TagRequestDto requestDto, User user);


    /*
     * 태그 수정
     * @param requestDto 태그 수정 요청 정보
     * @param user 사용자 권한 토큰
     * @return responseDto 태그 수정 결과
     * */
    public TagResponseDto updateTag(UpdateTagRequestDto requestDto, User user);


    /*
     * 태그 생성
     * @param requestDto 태그 삭제 요청 정보
     * @param user 사용자 권한 토큰
     * @return responseDto 태그 삭제 결과
     * */
    public void deleteTag(TagRequestDto requestDto, User user);
}
