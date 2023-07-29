package com.sparta.dtogram.profile.service;

import com.sparta.dtogram.profile.dto.PasswordRequestDto;
import com.sparta.dtogram.profile.dto.ProfileRequestDto;
import com.sparta.dtogram.profile.dto.ProfileResponseDto;
import com.sparta.dtogram.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    /*
     * 프로필 조회
     * @param id 조회할 사용자의 ID
     * @return 조회한 프로필 결과
     */
    public ProfileResponseDto getProfile(Long id);

    /*
     * 프로필 수정
     * @param user 사용자 권한 토큰
     * @param requestDto 프로필 수정 요청 정보
     * @return 프로필 수정 결과
     */
    public ProfileResponseDto editProfile(User user, ProfileRequestDto requestDto);

    /*
     * 비밀번호 수정
     * @param user 사용자 권한 토큰
     * @param requestDto 비밀번호 수정 요청 정보
     * @return 비밀번호 수정 결과
     */
    public void editPassword(User user, PasswordRequestDto requestDto);

    /*
     * 프로필 이미지 수정
     * @param user 사용자 권한 토큰
     * @part image 수정할 프로필 이미지 파일
     */
    public void updateImage(User user, MultipartFile image);

    /*
     * 프로필 이미지 삭제
     * @param user 사용자 권한 토큰
     */
    public void deleteImage(User user);
}
