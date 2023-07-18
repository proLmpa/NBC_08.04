package com.sparta.dtogram.tag.service;

import com.sparta.dtogram.tag.dto.TagRequestDto;
import com.sparta.dtogram.tag.dto.TagResponseDto;
import com.sparta.dtogram.tag.dto.UpdateTagRequestDto;
import com.sparta.dtogram.tag.entity.Tag;
import com.sparta.dtogram.tag.repository.TagRepository;
import com.sparta.dtogram.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public TagResponseDto createTag(TagRequestDto requestDto, User user) {
        Tag tag = tagRepository.save(new Tag(requestDto, user));
        return new TagResponseDto(tag);
    }

    public TagResponseDto updateTag(UpdateTagRequestDto requestDto, User user) {
        Tag tag = tagRepository.findByTag(requestDto.getTag()).orElseThrow(() ->
                new NullPointerException("해당하는 태그가 없습니다.")
        );
        if (tag.getUser().getUsername().equals(user.getUsername())) {
            tag.update(requestDto);
            return new TagResponseDto(tag);
        } else{
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    public void deleteTag(TagRequestDto requestDto, User user) {
        Tag tag = tagRepository.findByTag(requestDto.getTag()).orElseThrow(() ->
                new NullPointerException("해당하는 태그가 없습니다.")
        );
        if (tag.getUser().getUsername().equals(user.getUsername())) {
            tagRepository.delete(tag);
        } else {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
