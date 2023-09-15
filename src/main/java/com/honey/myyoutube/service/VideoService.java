package com.honey.myyoutube.service;

import com.honey.myyoutube.dto.searchcondition.MonthlyVideoSearchCondition;
import com.honey.myyoutube.dto.searchcondition.VideoSearchCondition;
import com.honey.myyoutube.dto.view.DailyVideoSimple;
import com.honey.myyoutube.dto.view.MonthlyVideoSimple;
import com.honey.myyoutube.dto.view.VideoDetail;
import com.honey.myyoutube.dto.view.TodayVideoSimple;
import com.honey.myyoutube.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.ZoneId;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class VideoService {

    private final VideoRepository videoRepository;

    /**
     * 오늘 데이터 검색과 과거 데이터 검색 메서드가 각각 다름
     */
    public Page<DailyVideoSimple> searchVideoList(Pageable pageable, VideoSearchCondition condition) {
        Page<? extends DailyVideoSimple> result;
        if (isToday(condition.getSearchDate())) {
            result = videoRepository.findTodayVideoPageBySearchCondition(pageable, condition);
        } else {
            result = videoRepository.findBeforeDayVideoPageBySearchCondition(pageable, condition);
        }
        return (Page<DailyVideoSimple>) result;
    }

    private boolean isToday(LocalDate condition) {
        return condition.isEqual(LocalDate.now(ZoneId.of("Asia/Seoul")));
    }

    public VideoDetail getVideo(String videoId) {
        return videoRepository.findByVideoId(videoId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 비디오 입니다."));
    }

    public Page<MonthlyVideoSimple> searchMonthlyVideoList(Pageable pageable, MonthlyVideoSearchCondition condition) {
        return videoRepository.findMonthlyVideoPage(pageable, condition);
    }
}
