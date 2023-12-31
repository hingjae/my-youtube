package com.honey.myyoutube.controller.view;

import com.honey.myyoutube.dto.searchcondition.MonthlyCategorySearchCondition;
import com.honey.myyoutube.dto.searchcondition.MonthlyVideoSearchCondition;
import com.honey.myyoutube.dto.view.CategoryDto;
import com.honey.myyoutube.dto.view.MonthlyVideoSimple;
import com.honey.myyoutube.dto.view.YearMonthDto;
import com.honey.myyoutube.service.CategoryService;
import com.honey.myyoutube.service.MonthSearchService;
import com.honey.myyoutube.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/search-month")
@RequiredArgsConstructor
@Controller
public class MonthlyVideoViewController {

    private final MonthSearchService monthSearchService;
    private final CategoryService categoryService;
    private final VideoService videoService;

    /**
     * 현재 calendar 테이블에 존재하는 년-월 리스트를 불러오는 메서드
     */
    @GetMapping
    public String getYearMonthList(Model model) {
        List<YearMonthDto> yearMonthList = monthSearchService.searchMonth();
        model.addAttribute("yearMonthList", yearMonthList);
        return "year-month-list";
    }

    /**
     * 월별 인기동영상 검색 view
     */
    @GetMapping("/videos")
    public String getMonthlyVideo(
            @PageableDefault(size = 20, page = 0) Pageable pageable,
            @RequestParam("yearMonth") String yearMonth,
            String categoryId, Model model) {
        MonthlyVideoSearchCondition videoSearchCondition = MonthlyVideoSearchCondition.builder()
                .yearMonth(yearMonth)
                .categoryId(categoryId)
                .build();
        MonthlyCategorySearchCondition categorySearchCondition = MonthlyCategorySearchCondition.builder()
                .year(videoSearchCondition.getYear())
                .month(videoSearchCondition.getMonth())
                .build();

        Page<MonthlyVideoSimple> videos = videoService.searchMonthlyVideoList(pageable, videoSearchCondition);
        List<CategoryDto> categories = categoryService.searchMonthlyCategoryList(categorySearchCondition);
        YearMonthDto yearAndMonth = YearMonthDto.builder().yearMonthParam(yearMonth).build();
        model.addAttribute("year", yearAndMonth.getYear());
        model.addAttribute("month", yearAndMonth.getMonth());
        model.addAttribute("yearMonth", yearMonth);
        model.addAttribute("categories", categories);
        model.addAttribute("videos", videos);
        return "monthly-videos";
    }

}
