package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.dto.score.VideoScore;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.honey.myyoutube.domain.QTodayTrendingVideo.todayTrendingVideo;
import static com.honey.myyoutube.domain.QVideo.video;

@RequiredArgsConstructor
public class TodayTrendingVideoRepositoryImpl implements TodayTrendingVideoRepositoryCustom {

    private final JPAQueryFactory query;

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
