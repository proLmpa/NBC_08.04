package com.sparta.dtogram.user.service;

import com.sparta.dtogram.user.dto.ProfileResponseDto;
import com.sparta.dtogram.user.dto.SignupRequestDto;
import com.sparta.dtogram.user.entity.PasswordHistory;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.entity.UserRoleEnum;
import com.sparta.dtogram.user.repository.PasswordHistoryRepository;
import com.sparta.dtogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordHistoryRepository passwordHistoryRepository;


    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String nickname = requestDto.getNickname();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        Optional<User> checkNickname = userRepository.findByNickname(nickname);
        if(checkNickname.isPresent()) {
            throw new IllegalArgumentException("중복된 nickname입니다.");
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(requestDto, password, role);
        userRepository.save(user);

        // 사용했던 비밀번호 목록에 저장
        passwordHistoryRepository.save(new PasswordHistory(requestDto.getPassword(), user));
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(()->
                new NullPointerException("유저 정보를 찾을 수 없습니다.")
                );
    }

    public List<ProfileResponseDto> getAllUsers(){
        return userRepository.findAll()
                .stream().map(ProfileResponseDto::new).toList();
    }

    public List<ProfileResponseDto> getAllUsers(String nickname){
        return userRepository.findByNicknameContainsOrderByNicknameAsc(nickname)
                .stream().map(ProfileResponseDto::new).toList(); //todo 페이징 작업 추가?
    }
}