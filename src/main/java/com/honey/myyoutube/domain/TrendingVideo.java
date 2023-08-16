package com.honey.myyoutube.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@ToString(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "trending_video")
@Entity
public class TrendingVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    private double score;

    @Builder
    private TrendingVideo(Long id, Video video, Calendar calendar, double score) {
        this.id = id;
        this.video = video;
        this.calendar = calendar;
        this.score = score;
    }

    public static TrendingVideo of(Video video, Calendar calendar, double score) {
        return TrendingVideo.builder()
                .video(video)
                .calendar(calendar)
                .score(score)
                .build();
    }
}
