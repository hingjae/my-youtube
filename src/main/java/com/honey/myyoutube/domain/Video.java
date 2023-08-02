package com.honey.myyoutube.domain;

import lombok.*;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@ToString(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Video {

    @Id
    private String id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;


    private String title;
    @Lob private String description;
    private String thumbnails;
    private LocalDateTime publishedAt;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;

    @Builder
    public Video(String id, Category category, Channel channel, String title, String description, String thumbnails, LocalDateTime publishedAt, Long viewCount, Long likeCount, Long commentCount) {
        this.id = id;
        this.category = category;
        this.channel = channel;
        this.title = title;
        this.description = description;
        this.thumbnails = thumbnails;
        this.publishedAt = publishedAt;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
