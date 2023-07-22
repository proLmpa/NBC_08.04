package com.sparta.dtogram.follow.repository;

import com.sparta.dtogram.follow.entity.Follow;
import com.sparta.dtogram.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowingAndFollower(User following, User follower);
    List<Follow> findAllByFollowing_Id(Long id);
    List<Follow> findAllByFollower_Id(Long id);
}
