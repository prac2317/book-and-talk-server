package com.talk.book.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AladinApiProperties {

    @Value("${aladin.api.key}")
    private String apiKey;

    @Value("${aladin.api.url}")
    private String baseUrl;
}
