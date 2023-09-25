package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.config.QueryDslConfig;
import com.honey.myyoutube.domain.Category;
import com.honey.myyoutube.domain.Channel;
import com.honey.myyoutube.domain.TodayTrendingVideo;
import com.honey.myyoutube.domain.Video;
import com.honey.myyoutube.dto.score.VideoScore;
import com.honey.myyoutube.repository.CategoryRepository;
import com.honey.myyoutube.repository.ChannelRepository;
import com.honey.myyoutube.repository.TodayTrendingVideoRepository;
import com.honey.myyoutube.repository.VideoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Import(QueryDslConfig.class)
@DataJpaTest
class TodayTrendingVideoRepositoryImplTest {

    @Autowired private VideoRepository videoRepository;
    @Autowired private ChannelRepository channelRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private TodayTrendingVideoRepository todayTrendingVideoRepository;

    @DisplayName("오늘 인기 동영상 점수 평균 계산후 내림차순 정렬")
    @Test
    void test1() {
        saveData();
        List<VideoScore> videoScore = todayTrendingVideoRepository.getVideoScore();
        assertThat(videoRepository.count()).isEqualTo(videoScore.size());
        assertThat(
                videoScore.get(0).getScore() >= videoScore.get(1).getScore() &&
                videoScore.get(1).getScore() >= videoScore.get(2).getScore() &&
                videoScore.get(2).getScore() >= videoScore.get(3).getScore() &&
                videoScore.get(3).getScore() >= videoScore.get(4).getScore() &&
                videoScore.get(4).getScore() >= videoScore.get(5).getScore()
        ).isTrue();

        int lastIndex = videoScore.size() - 1;
        assertThat(
                videoScore.get(lastIndex).getScore() <= videoScore.get(lastIndex - 1).getScore() &&
                videoScore.get(lastIndex - 1).getScore() <= videoScore.get(lastIndex - 2).getScore() &&
                videoScore.get(lastIndex - 2).getScore() <= videoScore.get(lastIndex - 3).getScore() &&
                videoScore.get(lastIndex - 3).getScore() <= videoScore.get(lastIndex - 4).getScore() &&
                videoScore.get(lastIndex - 4).getScore() <= videoScore.get(lastIndex - 5).getScore()
        ).isTrue();
        for (VideoScore score : videoScore) {
            System.out.print("score.getVideoId() = " + score.getVideoId());
            System.out.println("score.getScore() = " + score.getScore());
        }
    }

    private void saveData() {
        saveChannel();
        saveCategory();
        saveVideo();
        saveTodayTrendingVideo();
    }

    private void saveTodayTrendingVideo() {
        List<TodayTrendingVideo> todayTrendingVideoList = new ArrayList<>();
        LocalDateTime localDateTime = LocalDateTime.of(2023, 9, 21, 10, 0, 0);

        for (int i = 0; i < 3; i++) {
            List<String> videoIdList = shuffleVideoId();
            for (int j = 1; j <= 200; j++) {
                todayTrendingVideoList.add(
                        TodayTrendingVideo.builder()
                                .trendTime(localDateTime)
                                .video(videoRepository.getReferenceById(videoIdList.get(j - 1)))
                                .score(j)
                                .build()
                );
            }
            localDateTime = localDateTime.plusHours(1);
        }
        todayTrendingVideoRepository.saveAllAndFlush(todayTrendingVideoList);
    }
    private List<String> shuffleVideoId() {
        List<Integer> numbers = IntStream.rangeClosed(0, 199)// 0 ~ 199포함.
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(numbers, new Random());
        return numbers.stream()
                .map(String::valueOf)
                .toList();
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


}