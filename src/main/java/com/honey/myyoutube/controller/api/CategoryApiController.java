package com.honey.myyoutube.controller.api;

import com.honey.myyoutube.dto.view.CategoryDto;
import com.honey.myyoutube.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

/**
 * 테스트 용 api
 * 날짜 별 인기동영상의 존재하는 카테고리, 카테고리 별 개수 조회용
 */
@RestController
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;

    /**
     * 검색하는 날짜에 존재하는 동영상의 모든 카테고리를 가져옴
     * @param searchDate 날짜로 검색
     * @return 카테고리 DTO - 카테고리id, 카테고리 이름, 존재하는 동영상 개수
     */
    @GetMapping("/api/categories")
    public ResponseEntity<List<CategoryDto>> getCategoryList(
            @RequestParam(name = "searchDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchDate
    ) {
        if (searchDate == null) {searchDate = LocalDate.now(ZoneId.of("Asia/Seoul"));}
        List<CategoryDto> categoryList = categoryService.searchCategoryList(searchDate);
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryList);
    }
}
