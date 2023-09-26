package com.honey.myyoutube.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UriBuilderTest {

    @Autowired private UriBuilder uriBuilder;

    @DisplayName("인기 동영상 호출 API URI Builder")
    @Test
    void test1() {
        int pageSize = 50;
        String pageToken = "pageToken";
        URI uri = uriBuilder.buildLoadVideoUri(pageSize, pageToken);

        assertThat(uri.getScheme()).isEqualTo("https");
        assertThat(uri.getHost()).isEqualTo("www.googleapis.com");
        assertThat(uri.getPath()).isEqualTo("/youtube/v3/videos");
        assertThat(uri.getQuery()).isEqualTo(
                "part=snippet&part=statistics&chart=mostPopular&maxResults=50&pageToken=pageToken&regionCode=kr&key=YouTubeApiKey"
        );
    }

    @DisplayName("카테고리 호출 API URI Builder")
    @Test
    void test2() {
        URI uri = uriBuilder.buildLoadCategoryUri();

        assertThat(uri.getScheme()).isEqualTo("https");
        assertThat(uri.getHost()).isEqualTo("www.googleapis.com");
        assertThat(uri.getPath()).isEqualTo("/youtube/v3/videoCategories");
        assertThat(uri.getQuery()).isEqualTo("part=snippet&hl=ko_KR&regionCode=kr&key=YouTubeApiKey");
    }
}