package com.honey.myyoutube.service;

import com.honey.myyoutube.config.YoutubeProperties;
import com.honey.myyoutube.dto.VideoCategoryResponse;
import com.honey.myyoutube.dto.VideoCategoryResponse.VideoCategoryDto;
import com.honey.myyoutube.dto.VideoResponse;
import com.honey.myyoutube.repository.VideoCategoryRepository;
import com.honey.myyoutube.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class YoutubeService {

    private final RestTemplate restTemplate;
    private final YoutubeProperties youtubeProperties;
    private final VideoRepository videoRepository;
    private final VideoCategoryRepository videoCategoryRepository;

    public VideoResponse loadVideos(int pageSize, String pageToken, LocalDateTime now) {
        VideoResponse response;
        while (true) {
            URI uri = buildLoadVideoUri(pageSize, pageToken);
            response = restTemplate.getForObject(uri, VideoResponse.class);
            saveVideos(response, now);
            String nextPageToken = response.getNextPageToken();
            if (nextPageToken == null) break;
            pageToken = nextPageToken;
        }
        return response;
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

    private void saveVideos(VideoResponse response, LocalDateTime now) {
        videoRepository.saveAll(
                response.getItems().stream()
                        .map(dto -> dto.toEntity(now))
                        .collect(Collectors.toList())
        );
    }

    public VideoCategoryResponse loadCategories() {
        URI uri = buildLoadCategoryUri();
        VideoCategoryResponse response = restTemplate.getForObject(uri, VideoCategoryResponse.class);
        saveCategories(response);
        return response;
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

    private void saveCategories(VideoCategoryResponse response) {
        videoCategoryRepository.saveAll(
                response.getItems().stream()
                        .map(VideoCategoryDto::toEntity)
                        .collect(Collectors.toList())
        );
    }

}
