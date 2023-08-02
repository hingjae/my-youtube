package com.honey.myyoutube.service;

import com.honey.myyoutube.dto.searchcondition.VideoSearchCondition;
import com.honey.myyoutube.dto.view.CategoryDto;
import com.honey.myyoutube.dto.view.VideoSimple;
import com.honey.myyoutube.repository.CategoryRepository;
import com.honey.myyoutube.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final LocalDateTime localDateTime;


    public Page<VideoSimple> searchVideoList(Pageable pageable, VideoSearchCondition condition) {
        Page<VideoSimple> result;
        if (isToday(condition.getSearchDate())) {
            result = videoRepository.findTodayVideoBySearchCondition(pageable, condition);
        } else {
            result = videoRepository.findBeforeDayVideoBySearchCondition(pageable, condition);
        }
        return result;
    }

    private boolean isToday(LocalDate condition) {
        return condition.isEqual(localDateTime.now().toLocalDate());
    }
}
