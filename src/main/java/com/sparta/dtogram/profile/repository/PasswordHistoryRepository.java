package com.sparta.dtogram.profile.repository;

import com.sparta.dtogram.profile.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    @Query("select ph from PasswordHistory ph where ph.user.id = :id order by ph.createdAt desc limit 3")
    List<PasswordHistory> get3RecentPasswords(Long id);
}
