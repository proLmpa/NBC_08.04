package com.sparta.dtogram.follow.service;

import com.sparta.dtogram.follow.entity.Follow;
import com.sparta.dtogram.follow.repository.FollowRepository;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public String followUser(Long followerId, Long followingId) throws IllegalAccessException {
        if(followingId.equals(followerId)) {
            throw new IllegalAccessException("자기 자신을 팔로우할 수 없습니다.");
        }

        User follower = findUser(followerId);   // 팔로우 주체
        User following = findUser(followingId); // 팔로우 대상
        Follow follow = followRepository.findByFollowingAndFollower(following, follower).orElse(null);
        if(follow == null) {
            Follow newFollow = new Follow(following, follower);
            followRepository.save(newFollow);
            return "해당 유저를 팔로우했습니다!";
        } else {
            follow.cancelFollow(following, follower);
            followRepository.delete(follow);
            return "해당 유저를 언팔로우했습니다!";
        }
    }

    // 자신을 팔로우 중인 사용자들만 가져오기
    public List<User> getFollowerList(User user) {
        List<Follow> followList = followRepository.findAllByFollowing_Id(user.getId());

        List<User> followers = new ArrayList<>();
        for (Follow follow : followList) {
            followers.add(follow.getFollower());
        }
        return followers;
    }

    //  자신이 팔로우 하고 있는 사용자들만 가져오기
    public List<User> getFollowingList(User user) {
        List<Follow> followList = followRepository.findAllByFollower_Id(user.getId()); // 내가 팔로워인 모든 관계의 합은 나의 팔로잉 숫자이다.

        List<User> followings = new ArrayList<>();
        for (Follow follow : followList) {
            followings.add(follow.getFollowing());
        }
        return followings;
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 유저입니다.")
        );
    }
}

