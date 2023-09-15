package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.dto.searchcondition.MonthlyVideoSearchCondition;
import com.honey.myyoutube.dto.searchcondition.VideoSearchCondition;
import com.honey.myyoutube.dto.view.BeforeDayVideoSimple;
import com.honey.myyoutube.dto.view.MonthlyVideoSimple;
import com.honey.myyoutube.dto.view.VideoDetail;
import com.honey.myyoutube.dto.view.TodayVideoSimple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VideoRepositoryCustom {
    Page<TodayVideoSimple> findTodayVideoPageBySearchCondition(Pageable pageable, VideoSearchCondition condition);

    Page<BeforeDayVideoSimple> findBeforeDayVideoPageBySearchCondition(Pageable pageable, VideoSearchCondition condition);

    Optional<VideoDetail> findByVideoId(String videoId);

    Page<MonthlyVideoSimple> findMonthlyVideoPage(Pageable pageable, MonthlyVideoSearchCondition condition);
}
