package com.honey.myyoutube.repository.custom;

import com.honey.myyoutube.config.QueryDslConfig;
import com.honey.myyoutube.domain.Calendar;
import com.honey.myyoutube.dto.view.YearMonthDto;
import com.honey.myyoutube.repository.CalendarRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Import(QueryDslConfig.class)
@DataJpaTest
class CalendarRepositoryImplTest {

    @Autowired
    private CalendarRepository calendarRepository;

    @DisplayName("현재 존재하는 년-월 모음(중복제거)")
    @Test
    void test1() {
        calendarRepository.saveAll(List.of(
                Calendar.builder().calendarDate(LocalDate.of(2023, 7, 1)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 7, 2)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 7, 3)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 7, 21)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 7, 22)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 7, 23)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 8, 1)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 8, 2)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 8, 3)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 8, 21)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 8, 22)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 8, 23)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 9, 1)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 9, 2)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 9, 3)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 9, 21)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 9, 22)).build(),
                Calendar.builder().calendarDate(LocalDate.of(2023, 9, 23)).build()
        ));
        List<YearMonthDto> yearMonthList = calendarRepository.findYearMonth();
        assertThat(yearMonthList).hasSize(3);
        assertThat(yearMonthList).containsExactly(
                new YearMonthDto(2023, 9),
                new YearMonthDto(2023, 8),
                new YearMonthDto(2023, 7)
        );
    }
}