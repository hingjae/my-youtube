package com.honey.myyoutube.service;

import com.honey.myyoutube.dto.view.YearMonthDto;
import com.honey.myyoutube.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MonthSearchService {

    private final CalendarRepository calendarRepository;

    public List<YearMonthDto> searchMonth() {
        return calendarRepository.findYearMonth();
    }
}
