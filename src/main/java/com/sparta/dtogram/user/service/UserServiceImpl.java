package com.sparta.dtogram.user.service;

import com.sparta.dtogram.common.error.DtogramErrorCode;
import com.sparta.dtogram.common.exception.DtogramException;
import com.sparta.dtogram.profile.entity.PasswordHistory;
import com.sparta.dtogram.profile.repository.PasswordHistoryRepository;
import com.sparta.dtogram.user.dto.SignupRequestDto;
import com.sparta.dtogram.user.dto.UserInfoDto;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.entity.UserRoleEnum;
import com.sparta.dtogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordHistoryRepository passwordHistoryRepository;


    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Override
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String nickname = requestDto.getNickname();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new DtogramException(DtogramErrorCode.IN_USED_USERNAME, null);
        }

        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if(checkNickname.isPresent()) {
            throw new DtogramException(DtogramErrorCode.IN_USED_NICKNAME, null);
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new DtogramException(DtogramErrorCode.IN_USED_EMAIL, null);
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new DtogramException(DtogramErrorCode.WRONG_ADMIN_TOKEN, null);
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username ,nickname, password, email, role);
        userRepository.save(user);

        // 사용했던 비밀번호 목록에 저장
        passwordHistoryRepository.save(new PasswordHistory(password, user));
    }

    @Override
    public UserInfoDto getUserInfo(User user) {
        return new UserInfoDto(user.getId(), user.getRole().equals(UserRoleEnum.ADMIN));
    }
}