package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.dto.view.YearMonthDto;

import java.util.List;

public interface CalendarRepositoryCustom {
    List<YearMonthDto> findYearMonth();
}
