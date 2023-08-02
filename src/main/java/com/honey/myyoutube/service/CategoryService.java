package com.honey.myyoutube.service;

import com.honey.myyoutube.dto.view.CategoryDto;
import com.honey.myyoutube.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {

    private final LocalDateTime localDateTime;

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> searchCategoryList(LocalDate searchDate) {
        List<CategoryDto> result;
        if (isToday(searchDate)) {
            result = categoryRepository.findTodayDataByCondition(searchDate);
        } else {
            result = categoryRepository.findBeforeDayDataByCondition(searchDate);
        }
        result.add(0, new CategoryDto("all", "모든 동영상", null));
        return result;
    }
    private boolean isToday(LocalDate condition) {
        return condition.isEqual(localDateTime.now().toLocalDate());
    }
}
