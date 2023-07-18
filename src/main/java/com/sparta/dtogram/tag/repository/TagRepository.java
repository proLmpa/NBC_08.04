package com.sparta.dtogram.tag.repository;

import com.sparta.dtogram.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
