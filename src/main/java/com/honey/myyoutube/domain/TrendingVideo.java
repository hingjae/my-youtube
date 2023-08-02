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

    @Builder
    private TrendingVideo(Long id, Video video, Calendar calendar) {
        this.id = id;
        this.video = video;
        this.calendar = calendar;
    }
}
