package com.honey.myyoutube.dto.view;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DailyVideoSimple {
    protected Long trendingVideoId;
    protected String id;
    protected String title;
    protected String thumbnails;
    protected LocalDateTime publishedAt;
    protected String channelTitle;
    protected Long viewCount;
}
