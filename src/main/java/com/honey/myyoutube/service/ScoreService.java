package com.honey.myyoutube.service;

import com.honey.myyoutube.domain.Calendar;
import com.honey.myyoutube.domain.TrendingVideo;
import com.honey.myyoutube.domain.Video;
import com.honey.myyoutube.dto.score.VideoScore;
import com.honey.myyoutube.repository.CalendarRepository;
import com.honey.myyoutube.repository.TodayTrendingVideoRepository;
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
    private final TodayTrendingVideoRepository todayTrendingVideoRepository;
    private final TrendingVideoRepository trendingVideoRepository;
    private final VideoRepository videoRepository;
    private final CalendarRepository calendarRepository;


    /**
     * 오늘 인기 비디오 데이터 평균 점수 계산 -> 점수는 과거 비디오 데이터를 조회할 때 정렬하기 위해 사용
     */
    public void processDailyData(LocalDate today) {
        List<VideoScore> videoScoreList = todayTrendingVideoRepository.getVideoScore(); // video id와 평균점수 get
        Calendar calendar = calendarRepository.save(Calendar.of(today));
        saveTodayTrendingVideo(videoScoreList, calendar);
        deleteTodayTrendingVideo(today);
    }

    private void saveTodayTrendingVideo(List<VideoScore> videoScoreList, Calendar calendar) {
        trendingVideoRepository.saveAll(
                videoScoreList.stream()
                        .map((videoScore) -> {
                            Video video = videoRepository.getReferenceById(videoScore.getVideoId());
                            double score = videoScore.getScore();
                            return TrendingVideo.of(video, calendar, score);
                        })
                        .collect(Collectors.toList())
        );
    }

    private void deleteTodayTrendingVideo(LocalDate today) {
        todayTrendingVideoRepository.deleteAll();
    }
}
