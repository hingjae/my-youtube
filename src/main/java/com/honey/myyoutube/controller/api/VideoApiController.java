package com.honey.myyoutube.controller.api;

import com.honey.myyoutube.dto.searchcondition.VideoSearchCondition;
import com.honey.myyoutube.dto.view.VideoSimple;
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
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class VideoApiController {

    private final VideoService videoService;
    private final LocalDateTime localDateTime;

    /**
     * DB에 저장된 비디오를 조건을 반영해서 검색한다.
     * @param searchDate 조건 : 날짜
     * @param categoryId 조건 : 카테고리
     * @param pageable
     * @return 오늘 날짜 데이터를 검색하는 경우 최신순으로, 과거 데이터를 검색하는 경우 조회수 순으로 리턴한다.
     */
    @GetMapping("/api/videos")
    public ResponseEntity<Page<VideoSimple>> getVideoList(
            @RequestParam(name = "searchDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchDate,
            @RequestParam(name = "categoryId", required = false) String categoryId,
            @PageableDefault(size = 20, page = 0) Pageable pageable
    ) {
        if (searchDate == null) {searchDate = localDateTime.toLocalDate().now();}
        Page<VideoSimple> videoSimples = videoService.searchVideoList(pageable, VideoSearchCondition.of(searchDate, categoryId));
        return ResponseEntity.status(HttpStatus.OK)
                .body(videoSimples);
    }
}