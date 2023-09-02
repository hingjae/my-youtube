package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.dto.searchcondition.MonthlyVideoSearchCondition;
import com.honey.myyoutube.dto.view.CategoryDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.honey.myyoutube.domain.QCalendar.calendar;
import static com.honey.myyoutube.domain.QCategory.category;
import static com.honey.myyoutube.domain.QTodayTrendingVideo.todayTrendingVideo;
import static com.honey.myyoutube.domain.QTrendingVideo.trendingVideo;
import static com.honey.myyoutube.domain.QVideo.video;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<CategoryDto> findTodayDataByCondition(LocalDate searchDate) {
        return query
                .select(Projections.constructor(CategoryDto.class,
                    category.id, category.title, video.count()
                ))
                .from(todayTrendingVideo)
                .join(todayTrendingVideo.video, video)
                .join(video.category, category)
                .where(todayTrendingVideo.trendTime.eq(JPAExpressions
                        .select(todayTrendingVideo.trendTime.max())
                        .from(todayTrendingVideo)
                ))
                .groupBy(category)
                .fetch();
    }

    @Override
    public List<CategoryDto> findBeforeDayDataByCondition(LocalDate searchDate) {
        return query
                .select(Projections.constructor(CategoryDto.class,
                        category.id, category.title, video.count()
                ))
                .from(trendingVideo)
                .join(trendingVideo.video, video)
                .join(trendingVideo.calendar, calendar)
                .join(video.category, category)
                .where(calendar.calendarDate.eq(searchDate))
                .groupBy(category)
                .fetch();
    }

    @Override
    public List<CategoryDto> findMonthlyDataByCondition(MonthlyVideoSearchCondition condition) {
        return query
                .select(Projections.constructor(CategoryDto.class,
                        category.id, category.title, video.id.countDistinct()
                ))
                .from(trendingVideo)
                .join(trendingVideo.video, video)
                .join(trendingVideo.calendar, calendar)
                .join(video.category, category)
                .where(
                        calendar.calendarDate.year().eq(condition.getYear()),
                        calendar.calendarDate.month().eq(condition.getMonth())
                )
                .groupBy(category)
                .fetch();
    }
}

