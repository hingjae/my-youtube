package com.honey.myyoutube.service;

import com.honey.myyoutube.dto.searchcondition.VideoSearchCondition;
import com.honey.myyoutube.dto.view.VideoDetail;
import com.honey.myyoutube.dto.view.VideoSimple;
import com.honey.myyoutube.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final LocalDateTime localDateTime;


    public Page<VideoSimple> searchVideoList(Pageable pageable, VideoSearchCondition condition) {
        Page<VideoSimple> result;
        if (isToday(condition.getSearchDate())) {
            result = videoRepository.findTodayVideoPageBySearchCondition(pageable, condition);
        } else {
            result = videoRepository.findBeforeDayVideoPageBySearchCondition(pageable, condition);
        }
        return result;
    }

    private boolean isToday(LocalDate condition) {
        return condition.isEqual(localDateTime.now().toLocalDate());
    }

    public VideoDetail getVideo(String videoId) {
        return videoRepository.findByVideoId(videoId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 비디오 입니다."));
    }
}
