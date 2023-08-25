package com.honey.myyoutube.dto.view;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MonthlyVideoSimple {
    private final String id;
    private final String title;
    private final String thumbnails;
    private final LocalDateTime publishedAt;
    private final String channelTitle;
    private final Long viewCount;

    private final double scoreAvg;

    public MonthlyVideoSimple(String id, String title, String thumbnails, LocalDateTime publishedAt, String channelTitle, Long viewCount, double scoreAvg) {
        this.id = id;
        this.title = title;
        this.thumbnails = thumbnails;
        this.publishedAt = publishedAt;
        this.channelTitle = channelTitle;
        this.viewCount = viewCount;
        this.scoreAvg = scoreAvg;
    }
}
