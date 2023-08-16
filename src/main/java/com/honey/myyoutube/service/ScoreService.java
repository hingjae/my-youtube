package com.honey.myyoutube.service;

import com.honey.myyoutube.domain.Calendar;
import com.honey.myyoutube.domain.TrendingVideo;
import com.honey.myyoutube.domain.Video;
import com.honey.myyoutube.dto.score.VideoScore;
import com.honey.myyoutube.repository.TrendingVideoRepository;
import com.honey.myyoutube.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ScoreService {
    private final TrendingVideoRepository trendingVideoRepository;
    private final VideoRepository videoRepository;
    public void processDailyData(LocalDate today) {
        List<VideoScore> videoScoreList = trendingVideoRepository.getVideoScore(today); // 오늘 비디오 별 평균 점수 리스트
        trendingVideoRepository.deleteTodayData(today); // 오늘 자 비디오 삭제
        Calendar calendar = Calendar.builder().calendarDate(today).build(); //
        trendingVideoRepository.saveAll(
                videoScoreList.stream()
                        .map((videoScore) -> {
                            Video video = videoRepository.getReferenceById(videoScore.getVideoId());
                            return TrendingVideo.of(video, calendar, videoScore.getScore());
                        })
                        .collect(Collectors.toList())
        );

    }
}
