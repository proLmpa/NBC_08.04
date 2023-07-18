package com.sparta.dtogram.reply.repository;

import com.sparta.dtogram.reply.entity.Reply;
import com.sparta.dtogram.reply.entity.ReplyLike;
import com.sparta.dtogram.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    Optional<ReplyLike> findByUserAndReply(User user, Reply reply) ;
}
