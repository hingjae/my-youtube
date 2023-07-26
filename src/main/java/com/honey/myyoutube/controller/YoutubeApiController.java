package com.honey.myyoutube.controller;

import com.honey.myyoutube.config.YoutubeProperties;
import com.honey.myyoutube.dto.VideoCategoryResponse;
import com.honey.myyoutube.dto.VideoResponse;
import com.honey.myyoutube.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class YoutubeApiController {

    private final RestTemplate restTemplate;
    private final YoutubeProperties youtubeProperties;
    private final YoutubeService youtubeService;

    @GetMapping("/api/youtube")
    public ResponseEntity<VideoResponse> loadVideos(
            @RequestParam(required = false, defaultValue = "50") int pageSize,
            @RequestParam(required = false) String pageToken,
            @RequestParam(required = false) LocalDateTime now) {
        URI uri = buildLoadVideoUri(pageSize, pageToken);
        VideoResponse response = restTemplate.getForObject(uri, VideoResponse.class);
        return ResponseEntity.ok().body(response);
    }

    private URI buildLoadVideoUri(int pageSize, String pageToken) {
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

    @GetMapping("/api/category")
    public ResponseEntity<VideoCategoryResponse> setCategory() {
        URI uri = buildLoadCategoryUri();
        VideoCategoryResponse response = restTemplate.getForObject(uri, VideoCategoryResponse.class);
        return ResponseEntity.ok().body(response);
    }

    private URI buildLoadCategoryUri() {
        return UriComponentsBuilder.fromHttpUrl(youtubeProperties.getVideoCategoryUrl())
                .queryParam("part", "snippet")
                .queryParam("hl", "ko_KR")
                .queryParam("regionCode", "kr")
                .queryParam("key", youtubeProperties.getKey())
                .build()
                .toUri();
    }
}
