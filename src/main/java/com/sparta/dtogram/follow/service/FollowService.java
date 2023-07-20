package com.sparta.dtogram.follow.service;

import com.sparta.dtogram.follow.entity.Follow;
import com.sparta.dtogram.follow.repository.FollowRepository;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;


    public void doFollow(User followingUser, Long followerUserId) {

        User followerUser = getUserById(followerUserId);
        if(Objects.equals(followerUser.getId(), followingUser.getId())){
            throw new IllegalArgumentException("자기 자신을 팔로우하는 요청입니다.");
        }

        Boolean existFollow = followRepository.existsByFollowingUserAndFollowerUser(followingUser, followerUser);
        if (!existFollow) {
            Follow newFollow = new Follow(followingUser, followerUser);
            followRepository.save(newFollow);
        } else {
            throw new IllegalArgumentException("이미 " + followerUser.getUsername() + "님을 팔로우 중입니다.}");
        }
    }


    public String unFollow(User followingUser, Long followerUserId) {

        User followerUser = getUserById(followerUserId);
        Follow existFollow = followRepository.findByFollowingUserAndFollowerUser(followingUser, followerUser)
                .orElseThrow(() -> new IllegalArgumentException("팔로우 정보가 없습니다."));

        if (existFollow != null) { // todo optional에서 걸렀기 때문에 null이 아니란 걸 믿고 if문을 제거할 수 있을까?
            followRepository.delete(existFollow);
        }

        return "{ \"statusCode\":200,\n" +
                "\"statusMessage\":\"" + followerUser.getUsername() + "을 언팔로우하였습니다.}";
    }


    public List<User> getFollowerList(User user) {
        List<Follow> followList = followRepository.findByFollowingUser_Id(user.getId());

        List<User> followerList = new ArrayList<>();
        for (Follow follow : followList) {
            followerList.add(follow.getFollowerUser());
        }
        return followerList;
    }

    public List<User> getFollowingList(User user) {
        List<Follow> followList = followRepository.findByFollowerUser_Id(user.getId()); // 내가 팔로워인 모든 관계의 합은 나의 팔로잉 숫자이다.

        List<User> followingList = new ArrayList<>();
        for (Follow follow : followList) {
            followingList.add(follow.getFollowerUser());
        }
        return followingList;
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("유저 정보가 없습니다.")
        );
    }
}

