package com.sparta.dtogram.tag.service;

import com.sparta.dtogram.common.error.DtogramErrorCode;
import com.sparta.dtogram.common.exception.DtogramException;
import com.sparta.dtogram.tag.dto.TagRequestDto;
import com.sparta.dtogram.tag.dto.TagResponseDto;
import com.sparta.dtogram.tag.dto.UpdateTagRequestDto;
import com.sparta.dtogram.tag.entity.Tag;
import com.sparta.dtogram.tag.repository.TagRepository;
import com.sparta.dtogram.user.entity.User;
import com.sparta.dtogram.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public TagResponseDto createTag(TagRequestDto requestDto, User user) {
        Tag tag = tagRepository.findByTagName(requestDto.getTag()).orElse(null);

        // 태그 중복 시 등록 안함
        if(tag != null) {
            throw new DtogramException(DtogramErrorCode.TAG_ALREADY_EXISTS, null);
        } else {
            tag = tagRepository.save(new Tag(requestDto, user));
        }
        return new TagResponseDto(tag);
    }

    @Transactional
    public TagResponseDto updateTag(UpdateTagRequestDto requestDto, User user) {
        Tag tag = findTag(requestDto.getTag());
        if (matchUser(tag, user)) {
            tag.update(requestDto);
            return new TagResponseDto(tag);
        } else{
            throw new DtogramException(DtogramErrorCode.UNAUTHORIZED_USER, null);
        }
    }

    @Transactional
    public void deleteTag(TagRequestDto requestDto, User user) {
        Tag tag = findTag(requestDto.getTag());
        if (matchUser(tag, user)) {
            tagRepository.delete(tag);
        } else {
            throw new DtogramException(DtogramErrorCode.UNAUTHORIZED_USER, null);
        }
    }

    private Tag findTag(String tag) {
        return tagRepository.findByTagName(tag).orElseThrow(() ->
                new DtogramException(DtogramErrorCode.TAG_NOT_FOUND, null)
        );
    }

    private boolean matchUser(Tag tag, User user) {
        return tag.getUser().getId().equals(user.getId()) || user.getRole().equals(UserRoleEnum.ADMIN);
    }
}
