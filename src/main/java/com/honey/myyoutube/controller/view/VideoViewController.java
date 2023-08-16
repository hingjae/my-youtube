package com.honey.myyoutube.controller.view;

import com.honey.myyoutube.controller.api.VideoApiController;
import com.honey.myyoutube.dto.searchcondition.VideoSearchCondition;
import com.honey.myyoutube.dto.view.CategoryDto;
import com.honey.myyoutube.dto.view.VideoDetail;
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
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * my youtube 홈 날짜와 카테고리를 입력해서 동영상을 검색할 수 있음.
     * 동영상 리스트의 첫 페이지는 템플릿 엔진을 사용하고, 2 페이지 부터는 VideoApiController를 사용해서
     * 비동기로 데이터를 가져와야함.
     * @param searchDate 검색 날짜
     * @param categoryId 카테고리 id
     * @param pageable 페이지
     * @return
     */
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

    /**
     * 비디오 썸네일 클릭시 반환되는 비디오 시청 뷰
     * @param videoId 단건조회할 때 사용되는 비디오 Id (유튜브 video 고유 id와 같다)
     * @return
     */
    @GetMapping("/{videoId}")
    public String getVideo(@PathVariable String videoId, Model model) {
        VideoDetail video = videoService.getVideo(videoId);
        model.addAttribute("video", video);
        return "video";
    }
}
