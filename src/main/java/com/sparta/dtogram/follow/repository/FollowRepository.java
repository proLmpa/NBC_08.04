package com.sparta.dtogram.follow.repository;

import com.sparta.dtogram.follow.entity.Follow;
import com.sparta.dtogram.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowingUserAndFollowerUser_Id(User followingUser, Long id);
    List<Follow> findByFollowingUser_Id(Long id);
    List<Follow> findByFollowerUser_Id(Long id);

    Optional<Follow> findByFollowingUserAndFollowerUser(User followingUser, User followerUser);
    Boolean existsByFollowingUserAndFollowerUser(User followingUser, User followerUser);
}
