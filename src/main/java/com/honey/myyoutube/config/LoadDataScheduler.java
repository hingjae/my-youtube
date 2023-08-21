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
    //TODO: localDateTime o

    /**
     * 매일 정해진 시간에 주기적으로 유튜브 api를 호출해 데이터를 불러와 저장한다.
     */
    @Scheduled(cron = "5 0 * * * *", zone = "Asia/Seoul")
    public void callLoadData() {
        loadDataService.loadVideos(FIXED_PAGE_SIZE, FIRST_PAGE_TOKEN_IS_NULL, LocalDateTime.now(ZoneId.of("Asia/Seoul")));
    }

    @Scheduled(cron = "40 59 23 * * *", zone = "Asia/Seoul")
    public void calculateScoreAverage() {
        scoreService.processDailyData(LocalDate.now(ZoneId.of("Asia/Seoul")));
    }

}
