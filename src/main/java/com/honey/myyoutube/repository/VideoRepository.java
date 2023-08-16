package com.honey.myyoutube.repository;

import com.honey.myyoutube.domain.Video;
import com.honey.myyoutube.repository.custom.VideoSearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, String>, VideoSearchRepository {
}
