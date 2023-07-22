package com.sparta.dtogram.profile.repository;

import com.sparta.dtogram.profile.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    List<PasswordHistory> findAllByOrderByCreatedAtAsc();

    Optional<PasswordHistory> findByPassword(String newPassword);
}
