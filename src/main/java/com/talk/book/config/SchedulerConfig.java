package com.talk.book.config;

import com.talk.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfig {

    private final BookService bookService;

    @Scheduled(cron = "0 0 0 * * 1", zone = "Asia/Seoul")
    public void run() {
        bookService.getAladinList("BestSeller");
        bookService.getAladinList("BlogBest");
        bookService.getAladinList("ItemNewAll");
    }
}
