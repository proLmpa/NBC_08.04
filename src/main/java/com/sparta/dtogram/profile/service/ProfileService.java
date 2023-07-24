package com.sparta.dtogram.profile.service;

import com.sparta.dtogram.common.service.S3Uploader;
import com.sparta.dtogram.profile.dto.PasswordRequestDto;
import com.sparta.dtogram.profile.dto.ProfileRequestDto;
import com.sparta.dtogram.profile.dto.ProfileResponseDto;
import com.sparta.dtogram.profile.entity.PasswordHistory;
import com.sparta.dtogram.profile.repository.PasswordHistoryRepository;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordHistoryRepository passwordHistoryRepository;

    @Autowired
    private S3Uploader s3Uploader;

    public ProfileResponseDto getProfile(Long id) {
        User user = findUser(id);

        return new ProfileResponseDto(user);
    }

    @Transactional
    public void editProfile(User user, ProfileRequestDto requestDto){
        User changed = findUser(user.getId());
        changed.updateProfile(requestDto);
    }

    @Transactional
    public void editPassword(User user, PasswordRequestDto requestDto) {
        User changed = findUser(user.getId());

        if (passwordEncoder.matches(requestDto.getPassword(), changed.getPassword())) {
            if (requestDto.getNewPassword1().equals(requestDto.getNewPassword2())) {
                boolean isUsed = passwordHistoryRepository.findByPassword(requestDto.getNewPassword2()).isPresent();
                if (!isUsed) {
                    changed.setPassword(passwordEncoder.encode(requestDto.getNewPassword2()));
                    // 새로운 비밀번호 사용 비밀번호 목록에 저장
                    passwordHistoryRepository.save(new PasswordHistory(requestDto.getNewPassword2(), user));
                    if (changed.getPasswordHistories().size() > 3) {
                        PasswordHistory oldestPassword = passwordHistoryRepository.findAllByOrderByCreatedAtAsc().get(0);
                        passwordHistoryRepository.delete(oldestPassword);
                    }
                } else {
                    throw new IllegalArgumentException("최근 사용한 비밀번호입니다.");
                }
            } else {
                throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public void updateImage(User user, MultipartFile image) {
        try {
            User foundUser = findUser(user.getId());
            String storedFileName = s3Uploader.upload(image, "images");
            foundUser.updateProfileImage(storedFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteImage(User user) {
        User foundUser = findUser(user.getId());
        foundUser.updateProfileImage(null);
    }

    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 유저가 존재하지 않습니다.")
        );
    }
}
