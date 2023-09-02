package com.honey.myyoutube.controller.api;

import com.honey.myyoutube.dto.searchcondition.MonthlyVideoSearchCondition;
import com.honey.myyoutube.dto.view.MonthlyVideoSimple;
import com.honey.myyoutube.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MonthlyVideoApiController {

    private final VideoService videoService;

    /**
     * 월별 인기동영상 api
     */
    @GetMapping("/api/monthly-videos")
    public ResponseEntity<Page<MonthlyVideoSimple>> getVideoList(
            @RequestParam(name = "yearMonth") String yearMonth,
            @RequestParam(name = "categoryId", required = false) String categoryId,
            @PageableDefault(size = 20, page = 0) Pageable pageable
    ) {
        MonthlyVideoSearchCondition condition = MonthlyVideoSearchCondition.builder()
                .yearMonth(yearMonth)
                .categoryId(categoryId)
                .build();
        Page<MonthlyVideoSimple> videos = videoService.searchMonthlyVideoList(pageable, condition);
        return ResponseEntity.status(HttpStatus.OK)
                .body(videos);
    }
}
