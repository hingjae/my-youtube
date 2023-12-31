package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.dto.searchcondition.MonthlyCategorySearchCondition;
import com.honey.myyoutube.dto.searchcondition.MonthlyVideoSearchCondition;
import com.honey.myyoutube.dto.view.CategoryDto;

import java.time.LocalDate;
import java.util.List;

public interface CategoryRepositoryCustom {
    List<CategoryDto> findTodayDataByCondition();

    List<CategoryDto> findBeforeDayDataByCondition(LocalDate date);

    List<CategoryDto> findMonthlyDataByCondition(MonthlyCategorySearchCondition condition);
}
