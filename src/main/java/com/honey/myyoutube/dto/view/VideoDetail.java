package com.honey.myyoutube.dto.view;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class VideoDetail {
    private String id;
    private String title;
    private String description;
    private LocalDateTime publishedAt;
    private Long viewCount;
    private String channelId;
    private String channelTitle;

    public VideoDetail(String id, String title, String description, LocalDateTime publishedAt, Long viewCount, String channelId, String channelTitle) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.viewCount = viewCount;
        this.channelId = channelId;
        this.channelTitle = channelTitle;
    }
}
