package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.dto.searchcondition.VideoSearchCondition;
import com.honey.myyoutube.dto.view.VideoSimple;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.honey.myyoutube.domain.QCalendar.calendar;
import static com.honey.myyoutube.domain.QCategory.category;
import static com.honey.myyoutube.domain.QChannel.channel;
import static com.honey.myyoutube.domain.QTrendingVideo.trendingVideo;
import static com.honey.myyoutube.domain.QVideo.video;
import static com.querydsl.jpa.JPAExpressions.select;
import static com.querydsl.jpa.JPAExpressions.selectDistinct;

public class VideoRepositoryImpl implements VideoRepositorySearch{

    private final JPAQueryFactory query;

    public VideoRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Page<VideoSimple> findTodayVideoBySearchCondition(Pageable pageable, VideoSearchCondition condition) {
        BooleanBuilder categoryBuilder = categoryCondition(condition);
        List<VideoSimple> content = query
                .selectDistinct(Projections.constructor(VideoSimple.class,
                        trendingVideo.id,
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
                        calendar.calendarDateTime.eq(
                                select(calendar.calendarDateTime.max())
                                .from(calendar))
                                .and(categoryBuilder)
                )
                .orderBy(trendingVideo.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //trendingVideo.count() 를 사용하면 count(trendingVideo.id) 로 처리됩니다.
        Long total = query
                .select(trendingVideo.count())
                .from(trendingVideo)
                .join(trendingVideo.video, video)
                .join(trendingVideo.calendar, calendar)
                .join(video.channel, channel)
                .join(video.category, category)
                .where(
                        calendar.calendarDateTime.eq(select(calendar.calendarDateTime.max()).from(calendar))
                                .and(categoryBuilder)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);

    }
    @Override
    public Page<VideoSimple> findBeforeDayVideoBySearchCondition(Pageable pageable, VideoSearchCondition condition) {
        BooleanBuilder categoryBuilder = categoryCondition(condition);
        List<VideoSimple> content = query
                .selectDistinct(Projections.constructor(VideoSimple.class,
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
                .orderBy(video.viewCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = query
                .select(video.count())
                .from(video)
                .where(
                        video.id.in(
                            selectDistinct(video.id)
                                    .from(trendingVideo)
                                    .join(trendingVideo.calendar, calendar)
                                    .join(trendingVideo.video, video)
                                    .join(video.category, category)
                                    .where(calendar.calendarDate.eq(condition.getSearchDate()).and(categoryBuilder))
                        )
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
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