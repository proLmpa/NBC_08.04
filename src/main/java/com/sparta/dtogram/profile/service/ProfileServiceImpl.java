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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final S3Uploader s3Uploader;

    @Override
    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(Long id) {
        User user = findUser(id);
        return new ProfileResponseDto(user);
    }

    @Override
    @Transactional
    public ProfileResponseDto editProfile(User user, ProfileRequestDto requestDto){
        User found = findUser(user.getId());
        found.updateProfile(requestDto);

        return new ProfileResponseDto(found);
    }

    @Override
    @Transactional
    public void editPassword(User user, PasswordRequestDto requestDto) {
        User found = findUser(user.getId());

        if (passwordEncoder.matches(requestDto.getPassword(), found.getPassword())) {
            if (requestDto.getNewPassword1().equals(requestDto.getNewPassword2())) {
                List<PasswordHistory> passwordHistories = passwordHistoryRepository.get3RecentPasswords(found.getId());
                for(PasswordHistory passwordHistory : passwordHistories) {
                    if(passwordEncoder.matches(requestDto.getNewPassword1(), passwordHistory.getPassword()))
                        throw new DtogramException(DtogramErrorCode.PASSWORD_RECENTLY_USED, null);
                }

                String newPassword = passwordEncoder.encode(requestDto.getNewPassword2());
                found.setPassword(newPassword);
                passwordHistoryRepository.save(new PasswordHistory(newPassword, user));
            } else {
                throw new DtogramException(DtogramErrorCode.NEW_PASSWORD_MISMATCHED, null);
            }
        } else {
            throw new DtogramException(DtogramErrorCode.WRONG_PASSWORD, null);
        }
    }

    @Override
    @Transactional
    public void updateImage(User user, MultipartFile image) {
        User foundUser = findUser(user.getId());
        String storedFileName = s3Uploader.uploadFile(image, foundUser.getImageUrl()).getBody();
        foundUser.updateProfileImage(storedFileName);
    }

    @Override
    @Transactional
    public void deleteImage(User user) {
        User foundUser = findUser(user.getId());
        foundUser.updateProfileImage(null);
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new DtogramException(DtogramErrorCode.USER_NOT_FOUND, null)
        );
    }
}
