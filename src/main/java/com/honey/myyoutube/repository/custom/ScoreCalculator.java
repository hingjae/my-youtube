package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.dto.score.VideoScore;

import java.util.List;

public interface ScoreCalculator {
    List<VideoScore> getVideoScore();

}
