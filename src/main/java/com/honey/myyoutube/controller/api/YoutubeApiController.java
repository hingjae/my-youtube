package com.honey.myyoutube.controller.api;

import com.honey.myyoutube.config.YoutubeProperties;
import com.honey.myyoutube.dto.youtubeapi.VideoCategoryResponse;
import com.honey.myyoutube.dto.youtubeapi.VideoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

/**
 * 유튜브 api 테스트용
 * 현재 시각 인기동영상 조회 or 키 값이 유효한지 검증 용도 (DB에 데이터 저장 X)
 */
@RequiredArgsConstructor
@RestController
public class YoutubeApiController {

    private final RestTemplate restTemplate;
    private final YoutubeProperties youtubeProperties;

    /**
     * 현재 인기 동영상 조회
     * @param pageSize 최대 200개의 데이터까지 호출 가능
     * @param pageToken 다음 페이지를 불러오기 위한 토큰. 마지막 페이지는 null
     * @return
     */
    @GetMapping("/api/youtube")
    public ResponseEntity<VideoResponse> loadVideos(
            @RequestParam(required = false, defaultValue = "50") int pageSize,
            @RequestParam(required = false) String pageToken) {
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

    /**
     * 유튜브 동영상 전체 카테고리 목록 조회
     * @return 카테고리 목록
     */
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
