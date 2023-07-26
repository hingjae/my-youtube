package com.honey.myyoutube;

import com.honey.myyoutube.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class InitData {

    private final YoutubeService youtubeService;

    @PostConstruct
    public void initCategoryData() {
        youtubeService.loadCategories();
    }
}
