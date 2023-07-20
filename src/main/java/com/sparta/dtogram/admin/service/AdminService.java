package com.sparta.dtogram.admin.service;

import com.sparta.dtogram.user.dto.ProfileRequestDto;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.entity.UserRoleEnum;
import com.sparta.dtogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;


    public void editProfileByAdmin(User userAdmin, Long targetId, ProfileRequestDto requestDto) {
        if(userAdmin.getRole()!= UserRoleEnum.ADMIN){
            throw new IllegalArgumentException("Admin 권한이 없습니다.");
        }

        User targetUser = userRepository.findById(targetId).orElseThrow(()->
                new IllegalArgumentException("해당 유저의 정보를 찾을 수 없습니다.")
                );

        targetUser.updateProfile(requestDto); //todo 기존 profile업데이트 기능을 계속 써도 될지?
    }

}
