package com.honey.myyoutube.dto.view;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class TodayVideoSimple extends DailyVideoSimple {
    private int score;

    public TodayVideoSimple(String id, String title, String thumbnails, LocalDateTime publishedAt, String channelTitle, Long viewCount, int score) {
        this.id = id;
        this.title = title;
        this.thumbnails = thumbnails;
        this.publishedAt = publishedAt;
        this.channelTitle = channelTitle;
        this.viewCount = viewCount;
        this.score = score;
    }

    public TodayVideoSimple(Long trendingVideoId, String id, String title, String thumbnails, LocalDateTime publishedAt, String channelTitle, Long viewCount) {
        this.trendingVideoId = trendingVideoId;
        this.id = id;
        this.title = title;
        this.thumbnails = thumbnails;
        this.publishedAt = publishedAt;
        this.channelTitle = channelTitle;
        this.viewCount = viewCount;
    }
}
