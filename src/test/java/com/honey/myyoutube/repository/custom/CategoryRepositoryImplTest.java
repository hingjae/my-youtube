package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.config.QueryDslConfig;
import com.honey.myyoutube.domain.*;
import com.honey.myyoutube.dto.view.CategoryDto;
import com.honey.myyoutube.repository.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(QueryDslConfig.class)
@DataJpaTest
class CategoryRepositoryImplTest {

    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ChannelRepository channelRepository;
    @Autowired private VideoRepository videoRepository;
    @Autowired private TodayTrendingVideoRepository todayTrendingVideoRepository;
    @Autowired private TrendingVideoRepository trendingVideoRepository;
    @Autowired private CalendarRepository calendarRepository;
    public static final int COUNT_OF_CATEGORY = 10;
    
    @DisplayName("오늘자 데이터의 카테고리 추출 - 10개 카테고리 중 9개만 존재함.")
    @Test
    void test1() {
        saveData1();
        List<CategoryDto> todayDataCategory = categoryRepository.findTodayDataByCondition();
        assertThat(todayDataCategory.size()).isEqualTo(COUNT_OF_CATEGORY - 1);
    }

    @DisplayName("오늘 자 데이터의 카테고리 추출 - 10개 중 모든 카테고리가 존재함.")
    @Test
    void test2() {
        saveData2();
        List<CategoryDto> todayDataCategory = categoryRepository.findTodayDataByCondition();
        assertThat(todayDataCategory.size()).isEqualTo(COUNT_OF_CATEGORY);
    }

    @DisplayName("과거 데이터 조회 - 10개 중 9개의 카테고리만 존재하는 경우")
    @Test
    void test3() {
        saveData3();
        LocalDate searchDate = LocalDate.of(2023, 9, 21);
        List<CategoryDto> categoryList = categoryRepository.findBeforeDayDataByCondition(searchDate);
        assertThat(categoryList.size()).isEqualTo(COUNT_OF_CATEGORY - 1);
    }

    @Disabled
    @DisplayName("과거 데이터 조회 - 10개 중 모든 카테고리가 존재하는 경우")
    @Test
    void test4() {
        Long calendarId = saveData4();
//        LocalDate searchDate = LocalDate.of(2023, 9, 21);
        Calendar calendar = calendarRepository.findById(calendarId).orElseThrow(() -> new EntityNotFoundException());
        List<CategoryDto> categoryList = categoryRepository.findBeforeDayDataByCondition(calendar.getCalendarDate());
        assertThat(categoryList.size()).isEqualTo(COUNT_OF_CATEGORY);
    }

    private Long saveData4() {
        saveCategory();
        saveChannel();
        saveVideo2();
        Long calendarId = saveCalendar();
        saveTrendingVideo();
        calendarRepository.flush();
        return calendarId;
    }

    private void saveData3() {
        saveCategory();
        saveChannel();
        saveVideo1();
        saveCalendar();
        saveTrendingVideo();
    }

    private Long saveCalendar() {
        List<Calendar> calendarList = new ArrayList<>();
        calendarList.add(Calendar.builder().id(1L).calendarDate(LocalDate.of(2023, 7, 19)).build());
        calendarList.add(Calendar.builder().id(2L).calendarDate(LocalDate.of(2023, 7, 20)).build());
        calendarList.add(Calendar.builder().id(3L).calendarDate(LocalDate.of(2023, 7, 21)).build());
        calendarList.add(Calendar.builder().id(4L).calendarDate(LocalDate.of(2023, 8, 19)).build());
        calendarList.add(Calendar.builder().id(5L).calendarDate(LocalDate.of(2023, 8, 20)).build());
        calendarList.add(Calendar.builder().id(6L).calendarDate(LocalDate.of(2023, 8, 21)).build());
        calendarList.add(Calendar.builder().id(7L).calendarDate(LocalDate.of(2023, 9, 19)).build());
        calendarList.add(Calendar.builder().id(8L).calendarDate(LocalDate.of(2023, 9, 20)).build());
        calendarRepository.saveAll(calendarList);
        Calendar calendar = calendarRepository.save(Calendar.builder().id(9L).calendarDate(LocalDate.of(2023, 9, 21)).build());
        return calendar.getId();
    }

    private void saveTrendingVideo() {
        List<TrendingVideo> trendingVideoList = new ArrayList<>();
        Long calendarId = 9L;
        Calendar calendar1 = calendarRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException());
        Calendar calendar2 = calendarRepository.findById(2L).orElseThrow(() -> new EntityNotFoundException());
        Calendar calendar3 = calendarRepository.findById(calendarId).orElseThrow(() -> new EntityNotFoundException());

        for (int i = 0; i < 100; i++) {
            trendingVideoList.add(
                    TrendingVideo.builder()
                            .video(videoRepository.getReferenceById(String.valueOf(i)))
                            .calendar(calendar1)
                            .score(Double.valueOf(String.valueOf(i)))
                            .build()
            );
        }
        for (int i = 0; i < 100; i++) {
            trendingVideoList.add(
                    TrendingVideo.builder()
                            .video(videoRepository.getReferenceById(String.valueOf(i)))
                            .calendar(calendar2)
                            .score(Double.valueOf(String.valueOf(i)))
                            .build()
            );
        }
        for (int i = 0; i < 100; i++) {
            trendingVideoList.add(
                    TrendingVideo.builder()
                            .video(videoRepository.getReferenceById(String.valueOf(i)))
                            .calendar(calendar3)
                            .score(Double.valueOf(String.valueOf(i)))
                            .build()
            );
        }
        trendingVideoRepository.saveAll(trendingVideoList);
    }

    private void saveData1() {
        saveCategory();
        saveChannel();
        saveVideo1();
        saveTodayTrendingVideo();
    }

    private void saveData2() {
        saveCategory();
        saveChannel();
        saveVideo2();
        saveTodayTrendingVideo();
    }

    private void saveVideo2() {
        List<Video> videoList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            videoList.add(
                    Video.builder()
                            .id(String.valueOf(i))
                            .title("title" + i)
                            .category(categoryRepository.getReferenceById(String.valueOf(i % (COUNT_OF_CATEGORY))))
                            .channel(channelRepository.getReferenceById(String.valueOf((int)(Math.random() * 10))))
                            .build()
            );
        }
        videoRepository.saveAll(videoList);
    }

    private void saveTodayTrendingVideo() {
        List<TodayTrendingVideo> todayTrendingVideoList = new ArrayList<>();
        LocalDateTime trendTime = LocalDateTime.of(2023, 9, 22, 1, 0, 0);
        for (int i = 0; i < 100; i++) {
            todayTrendingVideoList.add(
                    TodayTrendingVideo.builder()
                            .trendTime(trendTime)
                            .score(i)
                            .video(videoRepository.getReferenceById(String.valueOf(i)))
                            .build()
            );
        }
        todayTrendingVideoRepository.saveAll(todayTrendingVideoList);
    }

    private void saveVideo1() {
        List<Video> videoList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            videoList.add(
                    Video.builder()
                            .id(String.valueOf(i))
                            .title("title" + i)
                            .category(categoryRepository.getReferenceById(String.valueOf(i % (COUNT_OF_CATEGORY - 1)))) // 10종류의 카테고리중 9개 카테고리만 존재함.
                            .channel(channelRepository.getReferenceById(String.valueOf((int)(Math.random() * 10))))
                            .build()
            );
        }
        videoRepository.saveAll(videoList);
    }

    private void saveChannel() {
        List<Channel> channelList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            channelList.add(
                    Channel.builder()
                            .id(String.valueOf(i))
                            .title("channel" + i)
                            .build()
            );
        }
        channelRepository.saveAll(channelList);
    }

    private void saveCategory() {
        List<Category> categoryList = new ArrayList<>();
        for (int i = 0; i < COUNT_OF_CATEGORY; i++) {
            categoryList.add(
                    Category.builder()
                            .id(String.valueOf(i))
                            .title("category" + i)
                            .build()
            );
        }
        categoryRepository.saveAll(categoryList);
    }

}
