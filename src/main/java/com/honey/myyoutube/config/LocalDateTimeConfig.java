package com.honey.myyoutube.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Configuration
public class LocalDateTimeConfig {
    @Bean
    public LocalDateTime koreaLocalDateTime() {
        ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime koreaDateTime = ZonedDateTime.now(koreaZoneId);
        return koreaDateTime.toLocalDateTime();
    }
}
