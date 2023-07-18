package com.sparta.dtogram.follow.repository;

import com.sparta.dtogram.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollowingUser_Id(Long id);
    List<Follow> findByFollowerUser_Id(Long id);
    Optional<Follow> findByFollowerUser_IdAndFollowingUser_Id(Long id, Long id1);
}
