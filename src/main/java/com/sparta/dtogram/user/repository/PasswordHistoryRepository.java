package com.sparta.dtogram.user.repository;

import com.sparta.dtogram.user.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
}
