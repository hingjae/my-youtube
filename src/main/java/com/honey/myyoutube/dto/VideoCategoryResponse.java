package com.honey.myyoutube.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.honey.myyoutube.domain.VideoCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class VideoCategoryResponse {

    private List<VideoCategoryDto> items;

    @Getter @Setter
    public static class VideoCategoryDto {
        private String id;
        private SnippetDto snippet;

        @Getter @Setter
        public static class SnippetDto {
            private String title;
        }

        public VideoCategory toEntity() {
            return VideoCategory.builder()
                    .id(id)
                    .title(snippet.title)
                    .build();
        }
    }
}

