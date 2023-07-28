package com.sparta.dtogram.profile.service;

import com.sparta.dtogram.common.error.DtogramErrorCode;
import com.sparta.dtogram.common.exception.DtogramException;
import com.sparta.dtogram.common.service.S3Uploader;
import com.sparta.dtogram.profile.dto.PasswordRequestDto;
import com.sparta.dtogram.profile.dto.ProfileRequestDto;
import com.sparta.dtogram.profile.dto.ProfileResponseDto;
import com.sparta.dtogram.profile.entity.PasswordHistory;
import com.sparta.dtogram.profile.repository.PasswordHistoryRepository;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    private final S3Uploader s3Uploader;

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(Long id) {
        User user = findUser(id);
        return new ProfileResponseDto(user);
    }

    @Transactional
    public ProfileResponseDto editProfile(User user, ProfileRequestDto requestDto){
        User found = findUser(user.getId());
        found.updateProfile(requestDto);

        return new ProfileResponseDto(found);
    }

    @Transactional
    public void editPassword(User user, PasswordRequestDto requestDto) {
        User found = findUser(user.getId());

        if (passwordEncoder.matches(requestDto.getPassword(), found.getPassword())) {
            if (requestDto.getNewPassword1().equals(requestDto.getNewPassword2())) {
                boolean isUsed = passwordHistoryRepository.findByPassword(requestDto.getNewPassword2()).isPresent();
                if (!isUsed) {
                    found.setPassword(passwordEncoder.encode(requestDto.getNewPassword2()));
                    // 새로운 비밀번호 사용 비밀번호 목록에 저장
                    passwordHistoryRepository.save(new PasswordHistory(requestDto.getNewPassword2(), user));
                    if (found.getPasswordHistories().size() > 3) {
                        PasswordHistory oldestPassword = passwordHistoryRepository.findAllByOrderByCreatedAtAsc().get(0);
                        passwordHistoryRepository.delete(oldestPassword);
                    }
                } else {
                    throw new DtogramException(DtogramErrorCode.PASSWORD_RECENTLY_USED, null);
                }
            } else {
                throw new DtogramException(DtogramErrorCode.NEW_PASSWORD_MISMATCHED, null);
            }
        } else {
            throw new DtogramException(DtogramErrorCode.WRONG_PASSWORD, null);
        }
    }

    @Transactional
    public void updateImage(User user, MultipartFile image) {
        try {
            User foundUser = findUser(user.getId());
            String storedFileName = s3Uploader.upload(image, "images");
            foundUser.updateProfileImage(storedFileName);
        } catch (IOException e) {
            throw new DtogramException(DtogramErrorCode.S3_UPLOAD_FAILURE, null);
        }
    }

    @Transactional
    public void deleteImage(User user) {
        User foundUser = findUser(user.getId());
        foundUser.updateProfileImage(null);
    }

    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new DtogramException(DtogramErrorCode.USER_NOT_FOUND, null)
        );
    }
}
