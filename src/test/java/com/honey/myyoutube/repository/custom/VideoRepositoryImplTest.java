package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.config.QueryDslConfig;
import com.honey.myyoutube.domain.*;
import com.honey.myyoutube.dto.searchcondition.VideoSearchCondition;
import com.honey.myyoutube.dto.view.BeforeDayVideoSimple;
import com.honey.myyoutube.dto.view.TodayVideoSimple;
import com.honey.myyoutube.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
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
class VideoRepositoryImplTest {

    public static final int COUNT_OF_CHANNEL = 50;
    public static final int COUNT_OF_CATEGORY = 10;
    public static final int COUNT_OF_VIDEO = 500;
    public static final int COUNT_OF_TODAY_TRENDING_VIDEO = 600;
    public static final int COUNT_HOUR = 3;
    public static final int COUNT_PER_HOUR = 200;

    public static final int PAGE_SIZE = 20;
    public static final int FIRST_PAGE = 0;
    public static final int LAST_PAGE = 9;
    public static final int TODAY_MAX_SCORE = 200;
    public static final int TODAY_MIN_SCORE = 1;
    public static final int COUNT_OF_TRENDING_VIDEO = 220;
    public static final LocalDate SEARCH_DATE = LocalDate.of(2023, 9, 19);
    @Autowired private EntityManager em;
    @Autowired private VideoRepository videoRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ChannelRepository channelRepository;
    @Autowired private TodayTrendingVideoRepository todayTrendingVideoRepository;
    @Autowired private TrendingVideoRepository trendingVideoRepository;
    @Autowired private CalendarRepository calendarRepository;

    @DisplayName("오늘의 실시간 인기 급상승 비디오 조회 (첫번째 페이지) - score가 높은 순으로 반환 MAX_SCORE == 200")
    @Test
    void test1() {
        int firstIndex = 0;
        int secondIndex = 1;
        saveTodayData();
        Pageable pageable = PageRequest.of(FIRST_PAGE, PAGE_SIZE);
        Page<TodayVideoSimple> videoSimplePage = videoRepository.findTodayVideoPageBySearchCondition(pageable, VideoSearchCondition.of(null, null));
        assertThat(todayTrendingVideoRepository.count()).isEqualTo(COUNT_OF_TODAY_TRENDING_VIDEO); //오늘의 인기비디오 개수 체크
        assertThat(videoSimplePage.getContent().size()).isEqualTo(PAGE_SIZE); // 페이지 사이즈 체크
        assertThat(videoSimplePage.getContent().get(firstIndex).getScore()).isEqualTo(TODAY_MAX_SCORE); // 첫번째 요소의 점수는 최고 점수인 200점
        assertThat(videoSimplePage.getContent().get(secondIndex).getScore()).isEqualTo(TODAY_MAX_SCORE - 1); // 두번 째는 199점
    }

    @DisplayName("오늘의 실시간 인기 급상승 비디오 조회 (마지막 페이지) - MIN_SCORE == 1")
    @Test
    void test2() {
        int lastIndex = 19; // 마지막 페이지 인덱스
        saveTodayData();
        Pageable pageable = PageRequest.of(LAST_PAGE, PAGE_SIZE);
        Page<TodayVideoSimple> videoSimplePage = videoRepository.findTodayVideoPageBySearchCondition(pageable, VideoSearchCondition.of(null, null));
        List<TodayVideoSimple> videos = videoSimplePage.getContent();
        assertThat(videos.size()).isEqualTo(PAGE_SIZE); // 페이지 사이즈 체크
        assertThat(videos.get(lastIndex).getScore()).isEqualTo(TODAY_MIN_SCORE); // 마지막 인덱스의 점수는 최소 점수인 1점이다.
    }

    @DisplayName("오늘의 실시간 인기 급상승 비디오 검색조건에 카테고리 아이디 넣어서 조회 - TodayTrendingVideo 중 가장 최근 시간대의 비디오를 가져옴")
    @Test
    void test3() {
        saveTodayData();
        String searchCategoryId = "1";

        //오늘의 실시간 인기 동영상 중 카테고리 ID가 1인 동영상 개수를 가져오는 쿼리
        Long countOfSearchedVideoByCategoryId = em.createQuery(
                        "select count(distinct v.id) " +
                                "from TodayTrendingVideo ttv " +
                                "join ttv.video v " +
                                "join v.category cate " +
                                "where cate.id = :id " +
                                "and ttv.trendTime = " +
                                    "(select max(ttv2.trendTime) " +
                                        "from TodayTrendingVideo ttv2)", Long.class)
                .setParameter("id", searchCategoryId)
                .getSingleResult();

        Pageable pageable = PageRequest.of(FIRST_PAGE, PAGE_SIZE);
        Page<TodayVideoSimple> videoPage = videoRepository.findTodayVideoPageBySearchCondition(pageable, VideoSearchCondition.of(null, searchCategoryId));

        List<TodayVideoSimple> videos = videoPage.getContent();
        assertThat(videoPage.getTotalElements()).isEqualTo(countOfSearchedVideoByCategoryId); // 카운트 쿼리와 메서드 결과의 개수가 일치하는 지 체크

        for (TodayVideoSimple videoSimple : videos) {
            String videoId = videoSimple.getId();
            Video video = videoRepository.findById(videoId).orElseThrow();
            assertThat(video.getCategory().getId()).isEqualTo(searchCategoryId); // 가져온 동영상의 카테고리 아이디가 "1"인지 체크
        }
    }

    @Disabled
    @DisplayName("과거 데이터 날짜 검색")
    @Test
    void test4() {
        saveCategory();
        saveChannel();
        saveBeforeDayVideo();
        saveCalendar();
        saveTrendingVideo();
        Long videoCount = em.createQuery(
                        "select count(distinct v.id) " +
                                "from TrendingVideo tv " +
                                "join tv.video v " +
                                "join tv.calendar cal " +
                                "where cal.calendarDate = :searchDate", Long.class
                )
                .setParameter("searchDate", SEARCH_DATE)
                .getSingleResult();
        Page<BeforeDayVideoSimple> videoSimplePage = videoRepository.findBeforeDayVideoPageBySearchCondition(PageRequest.of(FIRST_PAGE, PAGE_SIZE), VideoSearchCondition.of(SEARCH_DATE, null));
        List<BeforeDayVideoSimple> content = videoSimplePage.getContent();
        assertThat(videoSimplePage.getTotalElements()).isEqualTo(videoCount);
        assertThat(
                content.get(0).getScoreAvg() >= content.get(1).getScoreAvg() &&
                content.get(1).getScoreAvg() >= content.get(2).getScoreAvg() &&
                content.get(2).getScoreAvg() >= content.get(3).getScoreAvg() &&
                content.get(3).getScoreAvg() >= content.get(4).getScoreAvg() &&
                content.get(4).getScoreAvg() >= content.get(5).getScoreAvg()
        ).isTrue();
    }

    private void saveBeforeDayData() {

    }

    private void saveTrendingVideo() {
        List<String> videoIdList = shuffleVideoId2();
        List<TrendingVideo> trendingVideos = new ArrayList<>();
        for (int i = 0; i < 220; i++) {

            trendingVideos.add(TrendingVideo.builder()
                    .id(Long.valueOf(i))
                    .calendar(Calendar.builder().id(1L).calendarDate(SEARCH_DATE).build())
                    .video(videoRepository.getReferenceById(videoIdList.get(i)))
                    .score(Math.random() * 200)
                    .build());

        }
        trendingVideoRepository.saveAll(trendingVideos);
        em.flush();
        em.clear();
    }

    private void saveCalendar() {
        calendarRepository.save(
                Calendar.builder()
                        .id(1L)
                        .calendarDate(SEARCH_DATE)
                        .build()
        );
        em.flush();
        em.clear();
    }

    private void saveBeforeDayVideo() {
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
                                    categoryRepository.getReferenceById(String.valueOf((long) (Math.random() * COUNT_OF_CATEGORY)))
                            )
                            .channel(
                                    channelRepository.getReferenceById(String.valueOf((long) (Math.random() * COUNT_OF_CHANNEL)))
                            )
                            .build()
            );
        }
        em.flush();
        em.clear();
    }

    private void saveTodayData() {
        saveCategory();
        saveChannel();
        saveTodayVideo();
        saveTodayTrendingVideo();
    }

    private void saveTodayTrendingVideo() {
        List<TodayTrendingVideo> todayTrendingVideos = new ArrayList<>();

        for (int i = 0; i < COUNT_HOUR; i++) { // 1시 2시 3시 데이터 수집
            int score = COUNT_PER_HOUR;
            LocalDateTime now = LocalDateTime.of(2023, 9, 15, 0 + i, 0, 0);
            List<String> videoIdList = shuffleVideoId(); // 비디오 순위 섞어서 시간당 개수만큼 리스트 뽑기
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

    // 0~500개의 비디오ID를 무작위로 섞고 인덱스 200까지 수집해 아이디 리스트를 만듦.
    private List<String> shuffleVideoId() {
        List<Integer> numbers = IntStream.rangeClosed(0, 499)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(numbers, new Random());
        List<String> videoIdList = numbers.stream()
                .map(String::valueOf)
                .limit(200)
                .toList();
        return videoIdList;
    }

    private List<String> shuffleVideoId2() {
        List<Integer> numbers = IntStream.rangeClosed(0, 499)
                .boxed()
                .collect(Collectors.toList());

        Collections.shuffle(numbers, new Random());
        List<String> videoIdList = numbers.stream()
                .map(String::valueOf)
                .limit(220)
                .toList();
        return videoIdList;
    }

    private void saveTodayVideo() {
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
        em.clear();
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