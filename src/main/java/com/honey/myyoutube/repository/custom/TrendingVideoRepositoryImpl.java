package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.domain.QCalendar;
import com.honey.myyoutube.domain.QTrendingVideo;
import com.honey.myyoutube.domain.QVideo;
import com.honey.myyoutube.dto.score.VideoScore;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static com.honey.myyoutube.domain.QCalendar.calendar;
import static com.honey.myyoutube.domain.QTrendingVideo.trendingVideo;
import static com.honey.myyoutube.domain.QVideo.video;

public class TrendingVideoRepositoryImpl implements ScoreCalculator {

    private final JPAQueryFactory query;

    public TrendingVideoRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    // TODO: TodayTrendingVideo 테이블을 대상으로 점수를 계산해야함.
    @Override
    public List<VideoScore> getVideoScore(LocalDate today) {
        return query
                .select(Projections.constructor(VideoScore.class,
                        video.id, trendingVideo.score.avg()
                ))
                .from(trendingVideo)
                .join(trendingVideo.video, video)
                .join(trendingVideo.calendar, calendar)
                .where(calendar.calendarDate.eq(today))
                .groupBy(video)
                .orderBy(trendingVideo.score.avg().desc())
                .fetch();
    }

    //TODO: 오늘꺼 비디오 점수 평균 계산이 끝나면 TodayTrendingVideo 테이블의 데이터 지우기.
    @Override
    public void deleteTodayData(LocalDate today) {
        query
                .delete(trendingVideo)
                .where(trendingVideo.calendar.calendarDate.eq(today))
                .execute();
    }
}
