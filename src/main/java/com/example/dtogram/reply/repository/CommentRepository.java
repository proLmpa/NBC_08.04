package com.example.dtogram.Reply.repository;

import com.sparta.myblogbackend.dto.CommentResponseDto;
import com.sparta.myblogbackend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
