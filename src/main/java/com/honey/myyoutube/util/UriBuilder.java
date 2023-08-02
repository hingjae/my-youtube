package com.honey.myyoutube.util;

import com.honey.myyoutube.config.YoutubeProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class UriBuilder {
    private final YoutubeProperties youtubeProperties;

    public URI buildLoadVideoUri(int pageSize, String pageToken) {
        return UriComponentsBuilder.fromHttpUrl(youtubeProperties.getVideoUrl())
                .queryParam("part", "snippet")
                .queryParam("part", "statistics")
                .queryParam("chart", "mostPopular")
                .queryParam("maxResults", pageSize)
                .queryParam("pageToken", pageToken)
                .queryParam("regionCode", "kr")
                .queryParam("key", youtubeProperties.getKey())
                .build()
                .toUri();
    }

    public URI buildLoadCategoryUri() {
        return UriComponentsBuilder.fromHttpUrl(youtubeProperties.getVideoCategoryUrl())
                .queryParam("part", "snippet")
                .queryParam("hl", "ko_KR")
                .queryParam("regionCode", "kr")
                .queryParam("key", youtubeProperties.getKey())
                .build()
                .toUri();

    }
}
