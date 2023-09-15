package com.honey.myyoutube.controller.api;

import com.honey.myyoutube.dto.searchcondition.VideoSearchCondition;
import com.honey.myyoutube.dto.view.DailyVideoSimple;
import com.honey.myyoutube.dto.view.TodayVideoSimple;
import com.honey.myyoutube.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * 동영상을 2 페이지부터 비동기로 가져올 때 이 api를 사용한다.
 * 날짜와 카테고리를 파라미터로 넣어 검색가능
 */
@RequiredArgsConstructor
@RestController
public class VideoApiController {

    private final VideoService videoService;

    /**
     * DB에 저장된 비디오를 조건을 반영해서 검색한다.
     * @param searchDate 조건 : 날짜
     * @param categoryId 조건 : 카테고리
     * @param pageable 페이지 offset과 size
     * @return 오늘 날짜 데이터를 검색하는 경우 최신순으로 과거 데이터의 경우 score 순으로.
     */
    @GetMapping("/api/videos")
    public ResponseEntity<Page<DailyVideoSimple>> getVideoList(
            @RequestParam(name = "searchDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchDate,
            @RequestParam(name = "categoryId", required = false) String categoryId,
            @PageableDefault(size = 20, page = 0) Pageable pageable
    ) {
        if (searchDate == null) {searchDate = LocalDate.now(ZoneId.of("Asia/Seoul"));}
        Page<DailyVideoSimple> videoSimples = videoService.searchVideoList(pageable, VideoSearchCondition.of(searchDate, categoryId));
        return ResponseEntity.status(HttpStatus.OK)
                .body(videoSimples);
    }
}
