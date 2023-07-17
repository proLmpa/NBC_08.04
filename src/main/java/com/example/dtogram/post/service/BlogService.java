package com.sparta.myblogbackend.service;

import com.sparta.myblogbackend.dto.BlogRequestDto;
import com.sparta.myblogbackend.dto.BlogResponseDto;
import com.sparta.myblogbackend.dto.UpdateBlogRequestDto;
import com.sparta.myblogbackend.entity.Blog;
import com.sparta.myblogbackend.entity.BlogLike;
import com.sparta.myblogbackend.entity.Comment;
import com.sparta.myblogbackend.entity.User;
import com.sparta.myblogbackend.repository.BlogLikeRepository;
import com.sparta.myblogbackend.repository.BlogRepository;
import com.sparta.myblogbackend.repository.CommentRepository;
import com.sparta.myblogbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;
    private final BlogLikeRepository blogLikeRepository;
    private final UserRepository userRepository;

    public BlogResponseDto createBlog(BlogRequestDto requestDto, User user) {
        Blog blog= blogRepository.save(new Blog(requestDto, user));

        return new BlogResponseDto(blog);
    }

    @Transactional(readOnly = true)
    public List<BlogResponseDto> getBlogs() {
        return blogRepository.findAllByOrderByModifiedAtDesc().stream().map(BlogResponseDto::new).toList();
    }

    @Transactional(readOnly = true)
    public List<BlogResponseDto> getBlogsByKeyword(String keyword) {
        if (keyword == null) {
            throw new RuntimeException("키워드를 입력해주세요");
        }
        return blogRepository.findAllByContentsContainingOrderByModifiedAtDesc(keyword).stream().map(BlogResponseDto::new).toList();
    }

    @Transactional
    public BlogResponseDto updateBlog(Long id, UpdateBlogRequestDto requestDto, User user) {
        Blog blog = findBlog(id);
        if (blog.getUsername().equals(user.getUsername())) {
            blog.update(requestDto);
        } else {
            throw new RuntimeException("작성자만 삭제/수정할 수 있습니다.");
        }
        return new BlogResponseDto(blog);
    }


    public void deleteBlog(Long id, User user) {
        Blog blog = findBlog(id);
        if (blog.getUsername().equals(user.getUsername())) {
            blogRepository.delete(blog);
        } else {
            throw new RuntimeException("작성자만 삭제/수정할 수 있습니다.");
        }
    }

    private Blog findBlog(Long id) {
        return blogRepository.findById(id).orElseThrow(() -> // null 체크
                new IllegalArgumentException("선택한 글는 존재하지 않습니다.")
        );
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 유저입니다.")
        );
    }

    @Transactional
    public String like(Long blogId, Long userId) {
        final String[] msg = {""};

        Blog blog = findBlog(blogId);
        User user = findUser(userId);

        Optional<BlogLike> isLike = blogLikeRepository.findByUserAndBlog(user, blog);

        isLike.ifPresentOrElse(
                like -> {
                    blogLikeRepository.delete(like);
                    blog.subLikeCount(like);
                    blog.updateLikeCount();
                    msg[0] = "좋아요 취소";
                },
                () -> {
                    BlogLike blogLike = new BlogLike(user, blog);

                    blogLike.mappingBlog(blog);
                    blogLike.mappingUser(user);
                    blog.updateLikeCount();

                    blogLikeRepository.save(blogLike);
                    msg[0] = "좋아요";
                }
        );
        return msg[0];
    }

    public boolean isLiked(Long blogId, Long userId) {
        Blog blog = findBlog(blogId);
        User user = userRepository.findById(userId).orElse(new User());
        Optional<BlogLike> isLike = blogLikeRepository.findByUserAndBlog(user, blog);
        boolean isLiked = BlogLike.isLikedBlog(isLike);
        return isLiked;
    }
}
