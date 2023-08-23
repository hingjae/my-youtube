package com.honey.myyoutube.service;

import com.honey.myyoutube.domain.*;
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
    private final ChannelRepository channelRepository;
    private final VideoRepository videoRepository;
    private final TodayTrendingVideoRepository todayTrendingVideoRepository;

    /**
     * 유튜브 인기동영상 api 호출
     * 동영상을 받아와 DB에 저장
     */
    public List<YoutubeVideoDto> loadVideos(int pageSize, String pageToken, LocalDateTime nowDateTime) {
        List<YoutubeVideoDto> response = youtubeApi.loadVideoApi(pageSize, pageToken);
        saveData(response, nowDateTime);
        return response;
    }

    /**
     * 동영상마다 1점씩 내림차순으로 점수를 매겨 저장
     */
    private void saveData(List<YoutubeVideoDto> response, LocalDateTime now) {
        int score = response.size();
        for (YoutubeVideoDto youtubeVideoDto : response) {
            Category category = checkCategory(youtubeVideoDto);
            Channel channel = channelRepository.save(youtubeVideoDto.toChannel());
            Video video = videoRepository.save(youtubeVideoDto.toVideoEntity(category, channel));
            todayTrendingVideoRepository.save(TodayTrendingVideo.of(video, now, score));
            score--;
        }
    }

    private Category checkCategory(YoutubeVideoDto videoDto) {
        String categoryId = videoDto.getSnippet().getCategoryId();
        return categoryRepository.findById(categoryId) // 카테고리를 찾고 1차 캐시에 저장.
                .orElseGet(() -> {
                    Category newCategory = Category.builder()
                            .id("others")
                            .title("기타")
                            .build();
                    return categoryRepository.save(newCategory);
                });
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
