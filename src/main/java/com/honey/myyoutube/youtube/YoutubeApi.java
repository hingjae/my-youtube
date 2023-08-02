package com.honey.myyoutube.youtube;

import com.honey.myyoutube.dto.youtubeapi.VideoCategoryResponse;
import com.honey.myyoutube.dto.youtubeapi.VideoResponse;
import com.honey.myyoutube.dto.youtubeapi.VideoResponse.YoutubeVideoDto;
import com.honey.myyoutube.util.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class YoutubeApi {
    private final RestTemplate restTemplate;
    private final UriBuilder uriBuilder;

    public List<YoutubeVideoDto> loadVideoApi(int pageSize, String pageToken) {
        List<YoutubeVideoDto> result = new ArrayList<>();
        while (true) {
            URI uri = uriBuilder.buildLoadVideoUri(pageSize, pageToken);
            VideoResponse response = restTemplate.getForObject(uri, VideoResponse.class);
            result.addAll(response.getItems());
            String nextPageToken = response.getNextPageToken();
            if (nextPageToken == null) break;
            pageToken = nextPageToken;
        }
        return result;
    }

    public VideoCategoryResponse loadCategoryApi() {
        URI uri = uriBuilder.buildLoadCategoryUri();
        return restTemplate.getForObject(uri, VideoCategoryResponse.class);
    }
}
