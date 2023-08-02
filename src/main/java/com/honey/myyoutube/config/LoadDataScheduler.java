package com.honey.myyoutube.config;

import com.honey.myyoutube.service.LoadDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class LoadDataScheduler {

    private final static int FIXED_PAGE_SIZE = 50;
    private final static String FIRST_PAGE_TOKEN_IS_NULL = null;
    private final LoadDataService loadDataService;
    private final LocalDateTime localDateTime;

    /**
     * 매일 정해진 시간에 주기적으로 유튜브 api를 호출해 데이터를 불러와 저장한다.
     */
    @Scheduled(cron = "0 10 0,6,9,12,15,18,21 * * *", zone = "Asia/Seoul")
    public void callScheduledMethodAt9AM() {
        loadData();
    }

    private void loadData() {
        loadDataService.loadVideos(FIXED_PAGE_SIZE, FIRST_PAGE_TOKEN_IS_NULL, localDateTime.now());
    }
}
