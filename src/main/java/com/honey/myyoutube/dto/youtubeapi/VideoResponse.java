package com.honey.myyoutube.dto.youtubeapi;

import com.honey.myyoutube.domain.*;
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
            private String title;
            private String description;
            private String channelId;
            private String channelTitle;
            private List<String> tags;
            private String categoryId;
            private Thumbnails thumbnails;

            @Getter @Setter
            static class Thumbnails {
                private High high;

                @Getter @Setter
                static class High {
                    private String url;
                }
            }
        }

        @Getter @Setter
        static class StatisticsDto {
            private long viewCount;
            private long likeCount;
            private long favoriteCount;
            private long commentCount;
        }

        public TrendingVideo toTrendingVideoEntity(Calendar now) {
            return TrendingVideo.builder()
                    .video(toVideoEntity())
                    .calendar(now)
                    .build();
        }

        public Video toVideoEntity() {
            return Video.builder()
                    .id(id)
                    .category(toCategory())
                    .channel(toChannel())
                    .title(snippet.title)
                    .description(snippet.description)
                    .thumbnails(snippet.thumbnails.high.url)
                    .publishedAt(LocalDateTime.parse(snippet.publishedAt, DateTimeFormatter.ISO_DATE_TIME))
                    .viewCount(statistics.viewCount)
                    .likeCount(statistics.likeCount)
                    .commentCount(statistics.commentCount)
                    .build();
        }

        private Category toCategory() {
            return Category.builder()
                    .id(snippet.categoryId)
                    .build();
        }

        public Channel toChannel() {
            return Channel.builder()
                    .id(snippet.channelId)
                    .title(snippet.channelTitle)
                    .build();
        }
    }

    @Getter @Setter
    static class PageInfoDto {
        private int totalResults;
        private int resultsPerPage;
    }

}