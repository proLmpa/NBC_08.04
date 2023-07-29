package com.sparta.dtogram.admin.service;

import com.sparta.dtogram.profile.dto.ProfileRequestDto;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.entity.UserRoleEnum;
import com.sparta.dtogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void editProfileByAdmin(User userAdmin, Long targetId, ProfileRequestDto requestDto){
        checkAdminRole(userAdmin);
        findUser(targetId).updateProfile(requestDto);
    }

    @Override
    @Transactional
    public void editRoleByAdmin(User userAdmin, Long targetId, String role) {
        checkAdminRole(userAdmin);

        UserRoleEnum newRole = UserRoleEnum.valueOf(role);

        findUser(targetId)
                .updateRole(newRole);

    }

    @Override
    @Transactional
    public void deleteUserByAdmin(User userAdmin, Long targetId) {
        checkAdminRole(userAdmin);
        userRepository.delete(findUser(targetId));
    }

    private void checkAdminRole(User userAdmin) {
        if (userAdmin.getRole() != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("Admin 권한이 없습니다.");
        }
    }

    private User findUser(Long targetId) {
        return userRepository.findById(targetId).orElseThrow(() ->
                new IllegalArgumentException("해당 유저의 정보를 찾을 수 없습니다.")
        );
    }
}
