package com.honey.myyoutube.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "youtube")
public class YoutubeProperties {
    @Getter @Setter private String videoUrl;
    @Getter @Setter private String videoCategoryUrl;
    @Getter @Setter private String key;
}
