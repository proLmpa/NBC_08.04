package com.sparta.dtogram.user.repository;

import com.sparta.dtogram.user.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    boolean existsByPassword(String password);
    List<PasswordHistory> findAllByOrderByCreatedAtAsc();
}
