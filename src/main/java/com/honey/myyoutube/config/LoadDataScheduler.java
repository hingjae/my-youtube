package com.honey.myyoutube.config;

import com.honey.myyoutube.service.LoadDataService;
import com.honey.myyoutube.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class LoadDataScheduler {

    private final static int FIXED_PAGE_SIZE = 50;
    private final static String FIRST_PAGE_TOKEN_IS_NULL = null;
    private final LoadDataService loadDataService;
    private final ScoreService scoreService;
    private final LocalDateTime localDateTime;

    /**
     * 매일 정해진 시간에 주기적으로 유튜브 api를 호출해 데이터를 불러와 저장한다.
     */
    @Scheduled(cron = "5 0 0,3,6,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23 * * *", zone = "Asia/Seoul")
    public void callLoadData() {
        loadVideo();
    }

    @Scheduled(cron = "50 59 23 * * *", zone = "Asia/Seoul")
    public void calculateScoreAverage() {
        scoreService.processDailyData(localDateTime.now().toLocalDate());
    }

    private void loadVideo() {
        loadDataService.loadVideos(FIXED_PAGE_SIZE, FIRST_PAGE_TOKEN_IS_NULL, localDateTime.now());
    }
}
