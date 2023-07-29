package com.sparta.dtogram.admin.service;

import com.sparta.dtogram.profile.dto.ProfileRequestDto;
import com.sparta.dtogram.user.entity.User;

public interface AdminService {
    /*
     * (관리자) 사용자 프로필 수정
     * @param userAdmin 관리자 권한 토큰
     * @param targetId 수정할 유저의 ID
     * @param requestDto 사용자 프로필 수정 요청 정보
     */
    public void editProfileByAdmin(User userAdmin, Long targetId, ProfileRequestDto requestDto);

    /*
     * (관리자) 사용자 권한 수정
     * @param userAdmin 관리자 권한 토큰
     * @param targetId 수정할 유저의 ID
     * @param role 사용자 권한 수정 요청 정보
     */
    public void editRoleByAdmin(User userAdmin, Long targetId, String role);

    /*
     * (관리자) 사용자 정보 삭제
     * @param userAdmin 관리자 권한 토큰
     * @param targetId 수정할 유저의 ID
     */
    public void deleteUserByAdmin(User userAdmin, Long targetId);
}
