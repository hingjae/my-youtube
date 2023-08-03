package com.honey.myyoutube.dto.view;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class VideoSimple {
    private Long trendingVideoId;
    private String id;
    private String title;
    private String thumbnails;
    private LocalDateTime publishedAt;
    private String channelTitle;
    private Long viewCount;

    public VideoSimple(String id, String title, String thumbnails, LocalDateTime publishedAt, String channelTitle, Long viewCount) {
        this.id = id;
        this.title = title;
        this.thumbnails = thumbnails;
        this.publishedAt = publishedAt;
        this.channelTitle = channelTitle;
        this.viewCount = viewCount;
    }

    public VideoSimple(Long trendingVideoId, String id, String title, String thumbnails, LocalDateTime publishedAt, String channelTitle, Long viewCount) {
        this.trendingVideoId = trendingVideoId;
        this.id = id;
        this.title = title;
        this.thumbnails = thumbnails;
        this.publishedAt = publishedAt;
        this.channelTitle = channelTitle;
        this.viewCount = viewCount;
    }
}
