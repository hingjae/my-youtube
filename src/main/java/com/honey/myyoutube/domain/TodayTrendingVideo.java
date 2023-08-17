package com.honey.myyoutube.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "today_trending_video")
@Entity
public class TodayTrendingVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    private LocalDateTime trendTime;

    private int score;

    @Builder
    private TodayTrendingVideo(Long id, Video video, LocalDateTime trendTime, int score) {
        this.id = id;
        this.video = video;
        this.trendTime = trendTime;
        this.score = score;
    }

    public static TodayTrendingVideo of(Video video, LocalDateTime trendTime, int score) {
        return TodayTrendingVideo.builder()
                .video(video)
                .trendTime(trendTime)
                .score(score)
                .build();
    }
}
