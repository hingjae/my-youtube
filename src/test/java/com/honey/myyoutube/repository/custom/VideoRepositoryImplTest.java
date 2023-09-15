package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.config.QueryDslConfig;
import com.honey.myyoutube.domain.Category;
import com.honey.myyoutube.domain.Channel;
import com.honey.myyoutube.domain.TodayTrendingVideo;
import com.honey.myyoutube.domain.Video;
import com.honey.myyoutube.dto.searchcondition.VideoSearchCondition;
import com.honey.myyoutube.dto.view.TodayVideoSimple;
import com.honey.myyoutube.repository.CategoryRepository;
import com.honey.myyoutube.repository.ChannelRepository;
import com.honey.myyoutube.repository.TodayTrendingVideoRepository;
import com.honey.myyoutube.repository.VideoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Import(QueryDslConfig.class)
@DataJpaTest
class VideoRepositoryImplTest {

    public static final int COUNT_OF_CHANNEL = 50;
    public static final int COUNT_OF_CATEGORY = 10;
    public static final int COUNT_OF_VIDEO = 500;
    public static final int COUNT_OF_TODAY_TRENDING_VIDEO = 600;
    public static final int COUNT_HOUR = 3;
    public static final int COUNT_PER_HOUR = 200;

    public static final int PAGE_SIZE = 20;
    public static final int TODAY_MAX_SCORE = 200;
    @Autowired private EntityManager em;
    @Autowired private VideoRepository videoRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ChannelRepository channelRepository;
    @Autowired private TodayTrendingVideoRepository todayTrendingVideoRepository;


    @DisplayName("오늘의 실시간 인기 급상승 비디오를 score가 높은 순으로 조회 - 오늘의 데이터를 조회할 때 MAX_SCORE == 200")
    @Test
    void test1() {
        saveDate();
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);
        Assertions.assertThat(todayTrendingVideoRepository.count()).isEqualTo(COUNT_OF_TODAY_TRENDING_VIDEO);
        Page<TodayVideoSimple> videoSimplePage = videoRepository.findTodayVideoPageBySearchCondition(pageable, VideoSearchCondition.of(null, null));
        Assertions.assertThat(videoSimplePage.getContent().size()).isEqualTo(PAGE_SIZE);
        Assertions.assertThat(videoSimplePage.getContent().get(0).getScore()).isEqualTo(TODAY_MAX_SCORE);
        Assertions.assertThat(videoSimplePage.getContent().get(1).getScore()).isEqualTo(TODAY_MAX_SCORE - 1);
    }

    private void saveDate() {
        saveCategory();
        saveChannel();
        saveVideo();
        saveTodayTrendingVideo();
    }

    private void saveTodayTrendingVideo() {
        List<TodayTrendingVideo> todayTrendingVideos = new ArrayList<>();

        for (int i = 0; i < COUNT_HOUR; i++) {
            int score = COUNT_PER_HOUR;
            LocalDateTime now = LocalDateTime.of(2023, 9, 15, 0 + i, 0, 0);
            List<String> videoIdList = shuffleVideoId();
            for (int j = 0; j < COUNT_PER_HOUR; j++) {
                todayTrendingVideos.add(
                        TodayTrendingVideo.builder()
                                .video(videoRepository.getReferenceById(videoIdList.get(j)))
                                .trendTime(now)
                                .score(score)
                                .build()
                );
                score--;
            }
        }
        todayTrendingVideoRepository.saveAll(todayTrendingVideos);
        em.flush();
        em.clear();
    }

    private List<String> shuffleVideoId() {
        List<Integer> numbers = IntStream.rangeClosed(0, 499)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(numbers, new Random());
        List<String> videoIdList = numbers.stream()
                .map(String::valueOf)
                .limit(COUNT_PER_HOUR)
                .toList();
        return videoIdList;
    }

    private void saveVideo() {
        for (int i = 0; i < COUNT_OF_VIDEO; i++) {
            em.persist(
                    Video.builder()
                            .id(String.valueOf(i))
                            .title("title" + i)
                            .description("description" + i)
                            .thumbnails("thumbnails"+i)
                            .publishedAt(LocalDateTime.now())
                            .viewCount((long) (Math.random() * 100))
                            .likeCount((long) (Math.random() * 100))
                            .commentCount((long) (Math.random() * 100))
                            .category(
//                                    Category.builder()
//                                            .id(String.valueOf((long) (Math.random() * COUNT_OF_CATEGORY)))
//                                            .build() // select 쿼리를 던짐.
                                    categoryRepository.getReferenceById(String.valueOf((long) (Math.random() * COUNT_OF_CATEGORY))) // select 쿼리를 안 던짐.
                            )
                            .channel(
//                                    Channel.builder()
//                                            .id(String.valueOf((long) (Math.random() * COUNT_OF_CHANNEL)))
//                                            .build()
                                    channelRepository.getReferenceById(String.valueOf((long) (Math.random() * COUNT_OF_CHANNEL)))
                            )
                            .build()
            );
        }
        em.flush();
    }

    private void saveChannel() {
        for (int i = 0; i < COUNT_OF_CHANNEL; i++) {
            em.persist(
                    Channel.builder()
                            .id(String.valueOf(i))
                            .title("channel" + i)
                            .build()
            );
        }
        em.flush();
        em.clear();
    }

    private void saveCategory() {
        for (int i = 0; i < COUNT_OF_CATEGORY; i++) {
            em.persist(
                    Category.builder()
                            .id(String.valueOf(i))
                            .title("category" + i)
                            .build()
            );
        }
        em.flush();
        em.clear();
    }
}