package com.sparta.dtogram.follow.service;

import com.sparta.dtogram.follow.entity.Follow;
import com.sparta.dtogram.follow.repository.FollowRepository;
import com.sparta.dtogram.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;


    public String doFollow(User followingUser, User followerUser) {


        //이미 팔로우가 되어있는지 검사.
        Follow existFollow = followRepository.findByFollowerUser_IdAndFollowingUser_Id(followingUser.getId(), followerUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("팔로우 정보가 없습니다."));

        if (existFollow == null) { // 위에서 Optional로 받아서 Null을 거르는 건데 이게 맞나.?

            Follow newFollow = new Follow(followingUser, followerUser);
            followRepository.save(newFollow);

        }

        return "팔로우 완료.";
    }


    public String unFollow(User followingUser, User followerUser) {

        //이미 팔로우가 되어있는지 검사.
        Follow existFollow = followRepository.findByFollowerUser_IdAndFollowingUser_Id(followingUser.getId(), followerUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("팔로우 정보가 없습니다."));

        if (existFollow != null) { // 위에서 Optional로 받아서 Null을 거르는 건데 이게 맞나.?
            followRepository.delete(existFollow);
        }

        return "언팔로우 완료.";
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
}

