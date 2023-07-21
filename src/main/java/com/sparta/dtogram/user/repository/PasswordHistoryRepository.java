package com.sparta.dtogram.user.repository;

import com.sparta.dtogram.user.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    List<PasswordHistory> findAllByOrderByCreatedAtAsc();

    Optional<PasswordHistory> findByPassword(String newPassword);
}
