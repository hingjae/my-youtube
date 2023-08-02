package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.dto.searchcondition.VideoSearchCondition;
import com.honey.myyoutube.dto.view.VideoSimple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VideoRepositorySearch {
    Page<VideoSimple> findTodayVideoBySearchCondition(Pageable pageable, VideoSearchCondition condition);

    Page<VideoSimple> findBeforeDayVideoBySearchCondition(Pageable pageable, VideoSearchCondition condition);
}
