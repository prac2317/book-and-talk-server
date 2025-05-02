package com.talk.book.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("https://book-and-talk-client-git-deploy-sjs-projects-c78fe08f.vercel.app/")
//                .allowedOrigins("http://localhost:5173")
//                .allowedOrigins("https://book-and-talk-client-2nurgfakp-sjs-projects-c78fe08f.vercel.app/")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
