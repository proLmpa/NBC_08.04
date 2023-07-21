package com.sparta.dtogram.user.repository;


import com.sparta.dtogram.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByNaverId(String naverId);
    List<User> findByNicknameContainsOrderByNicknameAsc(String nickname);
}
