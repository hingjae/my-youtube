package com.honey.myyoutube.config;

import com.honey.myyoutube.service.LoadDataService;
import com.honey.myyoutube.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class LoadDataScheduler {

    private final static int FIXED_PAGE_SIZE = 50;
    private final static String FIRST_PAGE_TOKEN_IS_NULL = null;
    private final LoadDataService loadDataService;
    private final ScoreService scoreService;

    /**
     * 3시간 간격으로 유튜브 api를 호출해 데이터를 불러와 저장한다.
     * 토큰없이(null) 유튜브 api를 호출했을 때 첫페이지를 불러온다.
     */
    @Scheduled(cron = "5 0 0,3,6,9,12,15,18,21 * * *", zone = "Asia/Seoul")
    public void loadVideoData() {
        loadDataService.loadVideos(FIXED_PAGE_SIZE, FIRST_PAGE_TOKEN_IS_NULL, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void loadCategoryData() {
        loadDataService.loadCategories();
    }

    /**
     * 매일 23시 59분에 today_trending_video 테이블의 평균 점수를 계산한다.
     */
    @Scheduled(cron = "40 59 23 * * *", zone = "Asia/Seoul")
    public void calculateScoreAverage() {
        scoreService.processDailyData(LocalDate.now(ZoneId.of("Asia/Seoul")));
    }

}
