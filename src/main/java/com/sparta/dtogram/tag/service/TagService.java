package com.sparta.dtogram.tag.service;

import com.sparta.dtogram.tag.dto.TagRequestDto;
import com.sparta.dtogram.tag.dto.TagResponseDto;
import com.sparta.dtogram.tag.entity.Tag;
import com.sparta.dtogram.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public TagResponseDto createTag(TagRequestDto requestDto) {
        Tag tag = tagRepository.save(new Tag(requestDto));
        return new TagResponseDto(tag);
    }
}
