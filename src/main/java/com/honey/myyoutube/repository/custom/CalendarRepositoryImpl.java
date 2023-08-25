package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.dto.view.YearMonthDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.honey.myyoutube.domain.QCalendar.calendar;

@RequiredArgsConstructor
public class CalendarRepositoryImpl implements CalendarRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<YearMonthDto> findYearMonth() {
        return query
                .selectDistinct(Projections.constructor(YearMonthDto.class,
                        calendar.calendarDate.year(), calendar.calendarDate.month()
                ))
                .from(calendar)
                .orderBy(calendar.calendarDate.year().desc(), calendar.calendarDate.month().desc())
                .fetch();
    }
}
