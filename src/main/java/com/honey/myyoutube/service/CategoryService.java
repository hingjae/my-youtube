package com.honey.myyoutube.service;

import com.honey.myyoutube.dto.searchcondition.MonthlyVideoSearchCondition;
import com.honey.myyoutube.dto.view.CategoryDto;
import com.honey.myyoutube.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 날짜별 비디오에 존재하는 카테고리 검색
     */
    public List<CategoryDto> searchCategoryList(LocalDate searchDate) {
        List<CategoryDto> result;
        if (isToday(searchDate)) {
            result = categoryRepository.findTodayDataByCondition();
        } else {
            result = categoryRepository.findBeforeDayDataByCondition(searchDate);
        }
        //TODO: if 문을 좀 더 우아하게 바꿀 수 있는 방법을 알아보자
        if (result.size() > 0) {
            result.add(0, new CategoryDto("all", "모든 동영상", null));
        }
        return result;
    }

    private boolean isToday(LocalDate condition) {
        return condition.isEqual(LocalDate.now(ZoneId.of("Asia/Seoul")));
    }

    public List<CategoryDto> searchMonthlyCategoryList(MonthlyVideoSearchCondition condition) {
        List<CategoryDto> result = categoryRepository.findMonthlyDataByCondition(condition);
        result.add(0, new CategoryDto("all", "모든 동영상", null));
        return result;
    }
}
