package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.dto.searchcondition.MonthlyVideoSearchCondition;
import com.honey.myyoutube.dto.searchcondition.VideoSearchCondition;
import com.honey.myyoutube.dto.view.MonthlyVideoSimple;
import com.honey.myyoutube.dto.view.VideoDetail;
import com.honey.myyoutube.dto.view.VideoSimple;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.honey.myyoutube.domain.QCalendar.calendar;
import static com.honey.myyoutube.domain.QCategory.category;
import static com.honey.myyoutube.domain.QChannel.channel;
import static com.honey.myyoutube.domain.QTodayTrendingVideo.todayTrendingVideo;
import static com.honey.myyoutube.domain.QTrendingVideo.trendingVideo;
import static com.honey.myyoutube.domain.QVideo.video;

@RequiredArgsConstructor
public class VideoRepositoryImpl implements VideoRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Page<VideoSimple> findTodayVideoPageBySearchCondition(Pageable pageable, VideoSearchCondition condition) {
        BooleanBuilder categoryBuilder = categoryCondition(condition);
        List<VideoSimple> content = query
                .select(Projections.constructor(VideoSimple.class,
                        video.id,
                        video.title,
                        video.thumbnails,
                        video.publishedAt,
                        channel.title,
                        video.viewCount
                ))
                .from(todayTrendingVideo)
                .join(todayTrendingVideo.video, video)
                .join(video.category, category)
                .join(video.channel, channel)
                .where(todayTrendingVideo.trendTime.eq(JPAExpressions
                                        .select(todayTrendingVideo.trendTime.max())
                                        .from(todayTrendingVideo)
                                )
                                .and(categoryBuilder)
                )
                .orderBy(todayTrendingVideo.score.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long total = query
                .select(video.count())
                .from(todayTrendingVideo)
                .join(todayTrendingVideo.video, video)
                .join(video.category, category)
                .where(
                        todayTrendingVideo.trendTime.eq(JPAExpressions
                                        .select(todayTrendingVideo.trendTime.max())
                                        .from(todayTrendingVideo)
                                )
                                .and(categoryBuilder)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);

    }

    @Override
    public Page<VideoSimple> findBeforeDayVideoPageBySearchCondition(Pageable pageable, VideoSearchCondition condition) {
        BooleanBuilder categoryBuilder = categoryCondition(condition);
        List<VideoSimple> content = query
                .select(Projections.constructor(VideoSimple.class,
                        video.id,
                        video.title,
                        video.thumbnails,
                        video.publishedAt,
                        channel.title,
                        video.viewCount
                ))
                .from(trendingVideo)
                .join(trendingVideo.video, video)
                .join(trendingVideo.calendar, calendar)
                .join(video.channel, channel)
                .join(video.category, category)
                .where(
                        calendar.calendarDate.eq(condition.getSearchDate()).and(categoryBuilder)
                )
                .orderBy(trendingVideo.score.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long total = query
                .select(video.count())
                .from(trendingVideo)
                .join(trendingVideo.video, video)
                .join(trendingVideo.calendar, calendar)
                .join(video.category, category)
                .where(calendar.calendarDate.eq(condition.getSearchDate()).and(categoryBuilder))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<VideoDetail> findByVideoId(String videoId) {
        VideoDetail videoDetail = query
                .select(Projections.constructor(VideoDetail.class,
                        video.id,
                        video.title,
                        video.description,
                        video.publishedAt,
                        video.viewCount,
                        channel.id,
                        channel.title
                ))
                .from(video)
                .join(video.channel, channel)
                .where(video.id.eq(videoId))
                .fetchOne();
        return Optional.ofNullable(videoDetail);
    }

    @Override
    public Page<MonthlyVideoSimple> findMonthlyVideoPage(Pageable pageable, MonthlyVideoSearchCondition condition) {
        List<MonthlyVideoSimple> content = query
                .select(Projections.constructor(MonthlyVideoSimple.class,
                        video.id,
                        video.title,
                        video.thumbnails,
                        video.publishedAt,
                        channel.title,
                        video.viewCount,
                        trendingVideo.score.avg()
                ))
                .from(trendingVideo)
                .join(trendingVideo.video, video)
                .join(video.channel, channel)
                .join(video.category, category)
                .join(trendingVideo.calendar, calendar)
                .where(
                        calendar.calendarDate.between(condition.getStartOfMonth(), condition.getEndOfMonth()),
                        categoryContains(condition.getCategoryId())
                )
                .groupBy(video)
                .orderBy(trendingVideo.score.avg().desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long total = query
                .select(video.countDistinct())
                .from(trendingVideo)
                .join(trendingVideo.video, video)
                .join(trendingVideo.calendar, calendar)
                .join(video.category, category)
                .where(
                        calendar.calendarDate.between(condition.getStartOfMonth(), condition.getEndOfMonth()),
                        categoryContains(condition.getCategoryId())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression categoryContains(String categoryId) {
        return categoryId != null && StringUtils.hasText(categoryId) ? category.id.eq(categoryId) : null;
    }

    private BooleanBuilder categoryCondition(VideoSearchCondition condition) {
        BooleanBuilder builder = new BooleanBuilder();
        String categoryId = condition.getCategoryId();
        if (categoryId != null && StringUtils.hasText(categoryId)) {
            builder.and(category.id.eq(categoryId));
        }
        return builder;
    }
}
