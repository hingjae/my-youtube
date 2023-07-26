package com.honey.myyoutube.repository;

import com.honey.myyoutube.domain.VideoCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoCategoryRepository extends JpaRepository<VideoCategory, String> {
}
