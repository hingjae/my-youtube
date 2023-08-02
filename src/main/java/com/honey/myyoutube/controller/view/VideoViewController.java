package com.honey.myyoutube.controller.view;

import com.honey.myyoutube.controller.api.VideoApiController;
import com.honey.myyoutube.dto.searchcondition.VideoSearchCondition;
import com.honey.myyoutube.dto.view.CategoryDto;
import com.honey.myyoutube.dto.view.VideoSimple;
import com.honey.myyoutube.service.CategoryService;
import com.honey.myyoutube.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/videos")
@RequiredArgsConstructor
@Controller
public class VideoViewController {

    private final LocalDateTime localDateTime;
    private final VideoService videoService;
    private final CategoryService categoryService;

    @GetMapping
    public String getVideos(
            @RequestParam(name = "searchDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate searchDate,
            @RequestParam(name = "categoryId", required = false) String categoryId,
            @PageableDefault(size = 20, page = 0) Pageable pageable,
            Model model
    ) {
        if (searchDate == null) {searchDate = localDateTime.now().toLocalDate();}
        Page<VideoSimple> videos = videoService.searchVideoList(pageable, VideoSearchCondition.of(searchDate, categoryId));
        List<CategoryDto> categories = categoryService.searchCategoryList(searchDate);
        model.addAttribute("searchDate", searchDate);
        model.addAttribute("categories", categories);
        model.addAttribute("videos", videos);
        return "videos";
    }
}
