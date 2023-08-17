package com.honey.myyoutube.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Configuration
public class LocalDateTimeConfig {
    @Bean
    public LocalDateTime koreaLocalDateTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }
}
