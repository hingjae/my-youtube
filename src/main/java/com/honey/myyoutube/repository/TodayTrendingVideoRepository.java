package com.honey.myyoutube.repository;

import com.honey.myyoutube.domain.TodayTrendingVideo;
import com.honey.myyoutube.repository.custom.ScoreCalculator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodayTrendingVideoRepository extends JpaRepository<TodayTrendingVideo, Long>, ScoreCalculator {
}
