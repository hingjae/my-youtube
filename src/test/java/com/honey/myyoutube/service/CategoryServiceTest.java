package com.honey.myyoutube.service;

import com.honey.myyoutube.dto.searchcondition.MonthlyCategorySearchCondition;
import com.honey.myyoutube.dto.view.CategoryDto;
import com.honey.myyoutube.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks private CategoryService categoryService;

    @Mock private CategoryRepository categoryRepository;

    @DisplayName("오늘 자 데이터 카테고리 검색")
    @Test
    void test1() {
        int categoryCount = 3;
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));

        List<CategoryDto> result = categoryList(categoryCount);;

        given(categoryRepository.findTodayDataByCondition()).willReturn(result);

        List<CategoryDto> categoryDtoList = categoryService.searchCategoryList(today);

        assertThat(categoryDtoList).isEqualTo(result);
        assertThat(categoryDtoList.size()).isEqualTo(categoryCount + 1);
        then(categoryRepository).should().findTodayDataByCondition();
    }

    @DisplayName("과거 데이터 카테고리 검색")
    @Test
    void test2() {
        int categoryCount = 3;
        LocalDate beforeDay = LocalDate.of(2023, 9, 21);

        List<CategoryDto> result = categoryList(categoryCount);


        given(categoryRepository.findBeforeDayDataByCondition(beforeDay)).willReturn(result);

        List<CategoryDto> categoryDtoList = categoryService.searchCategoryList(beforeDay);

        assertThat(categoryDtoList).isEqualTo(result);
        assertThat(categoryDtoList.size()).isEqualTo(categoryCount + 1);
        then(categoryRepository).should().findBeforeDayDataByCondition(beforeDay);
    }

    private static List<CategoryDto> categoryList(int count) {
        List<CategoryDto> dtos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            dtos.add(CategoryDto.of(String.valueOf(i), "title" + i, Long.valueOf(String.valueOf(i))));
        }
        return dtos;
    }

    @DisplayName("월별 카테고리 조회")
    @Test
    void test3() {
        int categoryCount = 10;
        MonthlyCategorySearchCondition condition = MonthlyCategorySearchCondition.builder()
                .year(2023)
                .month(9)
                .build();
        List<CategoryDto> categoryDtoList = categoryList(categoryCount);

        given(categoryRepository.findMonthlyDataByCondition(condition)).willReturn(categoryDtoList);

        List<CategoryDto> result = categoryService.searchMonthlyCategoryList(condition);

        assertThat(result.size()).isEqualTo(categoryCount + 1);

        then(categoryRepository).should().findMonthlyDataByCondition(condition);
    }

}