package com.honey.myyoutube;

import com.honey.myyoutube.service.LoadDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class InitData {
    private final LoadDataService loadDataService;

    @PostConstruct
    public void initCategoryData() {
        loadDataService.loadCategories();
    }
}
