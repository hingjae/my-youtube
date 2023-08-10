package com.honey.myyoutube.service;

import com.honey.myyoutube.domain.Calendar;
import com.honey.myyoutube.domain.Category;
import com.honey.myyoutube.dto.youtubeapi.VideoCategoryResponse;
import com.honey.myyoutube.dto.youtubeapi.VideoCategoryResponse.VideoCategoryDto;
import com.honey.myyoutube.dto.youtubeapi.VideoResponse.YoutubeVideoDto;
import com.honey.myyoutube.repository.*;
import com.honey.myyoutube.youtube.YoutubeApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class LoadDataService {

    private final YoutubeApi youtubeApi;
    private final CategoryRepository categoryRepository;
    private final TrendingVideoRepository trendingVideoRepository;
    private final ChannelRepository channelRepository;
    private final CalendarRepository calendarRepository;
    private final VideoRepository videoRepository;

    public List<YoutubeVideoDto> loadVideos(int pageSize, String pageToken, LocalDateTime nowDateTime) {
        List<YoutubeVideoDto> response = youtubeApi.loadVideoApi(pageSize, pageToken);
        saveData(response, nowDateTime);
        return response;
    }

    private void saveData(List<YoutubeVideoDto> response, LocalDateTime now) {
        Calendar nowCalendar = saveCalendar(now);
        response.stream().forEach(videoDto -> {
            checkCategory(videoDto);
            channelRepository.save(videoDto.toChannel());
            videoRepository.save(videoDto.toVideoEntity());
            trendingVideoRepository.save(videoDto.toTrendingVideoEntity(nowCalendar));
        });
    }

    private void checkCategory(YoutubeVideoDto videoDto) {
        String categoryId = videoDto.getSnippet().getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseGet(() -> {
                    Category newCategory = Category.builder()
                            .id("others")
                            .title("기타")
                            .build();
                    return categoryRepository.save(newCategory);
                });
    }

    private Calendar saveCalendar(LocalDateTime now) {
        return calendarRepository.save(
                Calendar.builder()
                        .calendarDate(now.toLocalDate())
                        .calendarDateTime(now)
                        .build()
                );
    }

    @Transactional
    public VideoCategoryResponse loadCategories() {
        VideoCategoryResponse response = youtubeApi.loadCategoryApi();
        saveCategories(response);
        return response;
    }
    private void saveCategories(VideoCategoryResponse response) {
        List<VideoCategoryDto> categoryList = response.getItems();
        categoryRepository.saveAll(
                categoryList.stream()
                        .map(VideoCategoryDto::toEntity)
                        .collect(Collectors.toList())
        );
    }

}
