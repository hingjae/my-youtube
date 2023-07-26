package com.honey.myyoutube.config;

import com.honey.myyoutube.controller.YoutubeApiController;
import com.honey.myyoutube.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class LoadDataScheduler {

    private final static int FIXED_PAGE_SIZE = 50;
    private final static String FIRST_PAGE_TOKEN_IS_NULL = null;
    private final YoutubeService youtubeService;
    private final LocalDateTime localDateTime;

    @Scheduled(cron = "0 10 9 * * *", zone = "Asia/Seoul")
    public void callScheduledMethodAt9AM() {
        loadData();
    }

    @Scheduled(cron = "0 10 21 * * *", zone = "Asia/Seoul")
    public void callScheduledMethodAt9PM() {
        loadData();
    }

    private void loadData() {
        youtubeService.loadVideos(
                FIXED_PAGE_SIZE,
                FIRST_PAGE_TOKEN_IS_NULL,
                LocalDateTime.of(
                        localDateTime.getYear(),
                        localDateTime.getMonthValue(),
                        localDateTime.getDayOfMonth(),
                        localDateTime.getHour(),
                        0,
                        0
                )
        );
    }
}
