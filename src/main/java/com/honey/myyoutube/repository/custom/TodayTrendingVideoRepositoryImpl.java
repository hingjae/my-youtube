package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.dto.score.VideoScore;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.honey.myyoutube.domain.QTodayTrendingVideo.todayTrendingVideo;
import static com.honey.myyoutube.domain.QVideo.video;

public class TodayTrendingVideoRepositoryImpl implements ScoreCalculator {

    private final JPAQueryFactory query;

    public TodayTrendingVideoRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<VideoScore> getVideoScore() {
        return query
                .selectDistinct(Projections.constructor(VideoScore.class,
                        video.id, todayTrendingVideo.score.avg()
                ))
                .from(todayTrendingVideo)
                .join(todayTrendingVideo.video, video)
                .groupBy(video.id)
                .orderBy(todayTrendingVideo.score.avg().desc())
                .fetch();
    }
}
