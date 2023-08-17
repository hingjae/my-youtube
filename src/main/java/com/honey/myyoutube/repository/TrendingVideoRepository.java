package com.honey.myyoutube.repository;

import com.honey.myyoutube.domain.TrendingVideo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrendingVideoRepository extends JpaRepository<TrendingVideo, Long> {
}