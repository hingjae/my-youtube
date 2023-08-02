package com.honey.myyoutube.dto.youtubeapi;

import com.honey.myyoutube.domain.Category;
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

        public Category toEntity() {
            return com.honey.myyoutube.domain.Category.builder()
                    .id(id)
                    .title(snippet.title)
                    .build();
        }
    }
}

