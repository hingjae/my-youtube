package com.honey.myyoutube.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private VideoCategory videoCategory;

    @Column(nullable = false) private String videoId;
    @Column(nullable = false) private String title;
    @Column(nullable = true) private String description;
    @Column(nullable = false) private LocalDateTime publishedAt;
    @Column(nullable = false) private String channelId;
    @Column(nullable = true) private Long viewCount;
    @Column(nullable = true) private Long likeCount;
    @Column(nullable = true) private Long commentCount;
    @Column(nullable = false) private LocalDateTime trendDateTime;

    @Builder
    private Video(VideoCategory videoCategory, String videoId, String title, String description, LocalDateTime publishedAt, String channelId, Long viewCount, Long likeCount, Long commentCount, LocalDateTime trendDateTime) {
        this.videoCategory = videoCategory;
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.publishedAt = publishedAt;
        this.channelId = channelId;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.trendDateTime = trendDateTime;
    }
}
