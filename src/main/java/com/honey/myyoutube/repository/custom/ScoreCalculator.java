package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.domain.TrendingVideo;
import com.honey.myyoutube.dto.score.VideoScore;

import java.time.LocalDate;
import java.util.List;

public interface ScoreCalculator {
    List<VideoScore> getVideoScore(LocalDate today);

    void deleteTodayData(LocalDate today);
}
