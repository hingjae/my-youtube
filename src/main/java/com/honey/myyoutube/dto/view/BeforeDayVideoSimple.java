package com.honey.myyoutube.dto.view;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
@ToString
@Getter
public class BeforeDayVideoSimple extends DailyVideoSimple{
    private double scoreAvg;

    public BeforeDayVideoSimple(Long trendingVideoId, String id, String title, String thumbnails, LocalDateTime publishedAt, String channelTitle, Long viewCount, double scoreAvg) {
        this.trendingVideoId = trendingVideoId;
        this.id = id;
        this.title = title;
        this.thumbnails = thumbnails;
        this.publishedAt = publishedAt;
        this.channelTitle = channelTitle;
        this.viewCount = viewCount;
        this.scoreAvg = scoreAvg;
    }

    public BeforeDayVideoSimple(String id, String title, String thumbnails, LocalDateTime publishedAt, String channelTitle, Long viewCount, double scoreAvg) {
        this.id = id;
        this.title = title;
        this.thumbnails = thumbnails;
        this.publishedAt = publishedAt;
        this.channelTitle = channelTitle;
        this.viewCount = viewCount;
        this.scoreAvg = scoreAvg;
    }
}
