package com.honey.myyoutube.service;

import com.honey.myyoutube.domain.Category;
import com.honey.myyoutube.domain.Channel;
import com.honey.myyoutube.domain.Video;
import com.honey.myyoutube.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ScoreServiceTest {

    @InjectMocks private ScoreService scoreService;

    @Mock private TodayTrendingVideoRepository todayTrendingVideoRepository;
    @Mock private TrendingVideoRepository trendingVideoRepository;
    @Mock private VideoRepository videoRepository;
    @Mock private CalendarRepository calendarRepository;

    @Mock private ChannelRepository channelRepository;
    @Mock private CategoryRepository categoryRepository;

    @DisplayName("오늘 데이터 점수 계산")
    @Test
    void test1() {
        saveData();
    }

    private void saveData() {
        saveCategory();
        saveChannel();
        saveVideo();
    }

    private void saveCategory() {
        List<Category> categoryList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            categoryList.add(
                    Category.builder()
                            .id(String.valueOf(i))
                            .title("category" + i)
                            .build()
            );
        }
        categoryRepository.saveAllAndFlush(categoryList);
    }

    private void saveChannel() {
        List<Channel> channelList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            channelList.add(
                    Channel.builder()
                            .id(String.valueOf(i))
                            .title("channel" + i)
                            .build()
            );
        }
        channelRepository.saveAllAndFlush(channelList);
    }

    private void saveVideo() {
        List<Video> videoList = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            videoList.add(
                    Video.builder()
                            .id(String.valueOf(i))
                            .title("video" + i)
                            .category(categoryRepository.getReferenceById(String.valueOf((int) (Math.random() * 20))))
                            .channel(channelRepository.getReferenceById(String.valueOf((int) (Math.random() * 50))))
                            .build()
            );
        }
        videoRepository.saveAllAndFlush(videoList);
    }
}