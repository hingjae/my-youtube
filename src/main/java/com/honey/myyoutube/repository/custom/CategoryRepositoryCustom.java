package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.dto.view.CategoryDto;

import java.time.LocalDate;
import java.util.List;

public interface CategoryRepositoryCustom {
    List<CategoryDto> findTodayDataByCondition(LocalDate date);

    List<CategoryDto> findBeforeDayDataByCondition(LocalDate date);
}
