package com.honey.myyoutube.dto;

import com.honey.myyoutube.domain.Video;
import com.honey.myyoutube.domain.VideoCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter @Setter
public class VideoResponse {
    private List<YoutubeVideoDto> items;
    private String nextPageToken;
    private PageInfoDto pageInfo;

    @Getter @Setter
    public static class YoutubeVideoDto {
        private String id;
        private SnippetDto snippet;
        private StatisticsDto statistics;

        @Getter @Setter
        static class SnippetDto {
            private String publishedAt;
            private String channelId;
            private String title;
            private String description;
            private String channelTitle;
            private List<String> tags;
            private String categoryId;
        }

        @Getter @Setter
        public static class StatisticsDto {
            private long viewCount;
            private long likeCount;
            private long favoriteCount;
            private long commentCount;
        }

        public Video toEntity(LocalDateTime now) {
            return Video.builder()
                    .videoId(id)
                    .title(snippet.getTitle())
                    .description(snippet.description)
                    .publishedAt(LocalDateTime.parse(snippet.publishedAt, DateTimeFormatter.ISO_DATE_TIME))
                    .channelId(snippet.channelId)
                    .videoCategory(VideoCategory.builder().id(snippet.categoryId).build())
                    .viewCount(statistics.viewCount)
                    .likeCount(statistics.likeCount)
                    .commentCount(statistics.commentCount)
                    .trendDateTime(now)
                    .build();
        }
    }

    @Getter @Setter
    public static class PageInfoDto {
        private int totalResults;
        private int resultsPerPage;
    }

}