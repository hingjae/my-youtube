package com.honey.myyoutube.controller.view;

import com.honey.myyoutube.service.CategoryService;
import com.honey.myyoutube.service.VideoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VideoViewController.class)
class VideoViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoService videoService;
    @MockBean
    private CategoryService categoryService;

    @DisplayName("비디오 페이지 요청")
    @Test
    void getVideos() throws Exception {
        mockMvc.perform(get("/videos")
                        .queryParam("searchDate", String.valueOf(LocalDate.of(2023,8,23)))
                        .queryParam("categoryId", "1")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("searchDate"))
                .andExpect(view().name("videos"))
                .andDo(print());
    }

}