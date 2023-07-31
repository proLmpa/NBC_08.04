package com.sparta.dtogram.post.repository;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.dtogram.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.dtogram.follow.entity.QFollow.follow;
import static com.sparta.dtogram.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> getFollowersPostsByUserId(Long userId, Pageable pageable) {
        var followersPosts = jpaQueryFactory
                .select(post)
                .from(post)
                .innerJoin(follow)
                .on(post.user.id.eq(follow.follower.id))
                .where(follow.following.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalSize = jpaQueryFactory
                .select(Wildcard.count)
                .from(post)
                .innerJoin(follow)
                .on(post.user.id.eq(follow.follower.id))
                .where(follow.following.id.eq(userId))
                .fetch().get(0);

        return PageableExecutionUtils.getPage(followersPosts, pageable, () -> totalSize);
    }

    @Override
    public Page<Post> getFollowingsPostsByUserId(Long userId, Pageable pageable) {
        var followingsPosts = jpaQueryFactory
                .select(post)
                .from(post)
                .innerJoin(follow)
                .on(post.user.id.eq(follow.following.id))
                .where(follow.follower.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalSize = jpaQueryFactory
                .select(Wildcard.count)
                .from(post)
                .innerJoin(follow)
                .on(post.user.id.eq(follow.following.id))
                .where(follow.follower.id.eq(userId))
                .fetch().get(0);

        return PageableExecutionUtils.getPage(followingsPosts, pageable, () -> totalSize);
    }

    @Override
    public List<Post> getPostsByTagId(Long tagId) {
        List<Post> tagedPosts = jpaQueryFactory
                .select(post)
                .from(post)
                .where(post.postTags.any().tag.id.eq(tagId))
                .fetch();

        tagedPosts.sort((p1, p2) -> p2.getModifiedAt().compareTo(p1.getModifiedAt()));
        return tagedPosts;
    }

    @Override
    public List<Post> getLikedPostsByUserId(Long userId) {
        List<Post> likedPosts = jpaQueryFactory
                .select(post)
                .from(post)
                .where(post.postLikes.any().user.id.eq(userId))
                .fetch();

        likedPosts.sort((p1, p2) -> p2.getModifiedAt().compareTo(p1.getModifiedAt()));
        return likedPosts;
    }
}
