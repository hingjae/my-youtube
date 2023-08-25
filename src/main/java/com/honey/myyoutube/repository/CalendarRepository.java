package com.honey.myyoutube.repository;

import com.honey.myyoutube.domain.Calendar;
import com.honey.myyoutube.repository.custom.CalendarRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long>, CalendarRepositoryCustom {
}
