package com.sparta.dtogram.user.service;

import com.sparta.dtogram.user.dto.PasswordRequestDto;
import com.sparta.dtogram.user.dto.ProfileRequestDto;
import com.sparta.dtogram.user.dto.ProfileResponseDto;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileResponseDto getProfile(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        );

        return new ProfileResponseDto(user);
    }

    @Transactional
    public void editProfile(User user, ProfileRequestDto requestDto) {
        User changed = userRepository.findById(user.getId()).orElseThrow(() ->
                new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        );
        changed.updateProfile(requestDto);
    }

    @Transactional
    public void editPassword(User user, PasswordRequestDto requestDto) {
        User changed = userRepository.findById(user.getId()).orElseThrow(() ->
                new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        );
        if (passwordEncoder.matches(requestDto.getPassword(), changed.getPassword())) {
            if (requestDto.getNewPassword1().equals(requestDto.getNewPassword2())) {
                changed.setPassword(passwordEncoder.encode(requestDto.getNewPassword2()));
            } else {
                throw new IllegalArgumentException("같은 비밀번호를 입력해주세요");
            }
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
