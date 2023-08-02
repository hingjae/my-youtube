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
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryApiController {

    private final LocalDateTime localDateTime;
    private final CategoryService categoryService;

    @GetMapping("/api/categories")
    public ResponseEntity<List<CategoryDto>> getCategoryList(
            @RequestParam(name = "searchDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchDate
    ) {
        if (searchDate == null) {searchDate = localDateTime.toLocalDate().now();}
        List<CategoryDto> categoryList = categoryService.searchCategoryList(searchDate);
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoryList);
    }
}
