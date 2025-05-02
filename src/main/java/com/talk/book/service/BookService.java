package com.talk.book.service;

import com.talk.book.config.AladinApiProperties;
import com.talk.book.domain.Book;
import com.talk.book.domain.BookCategory;
import com.talk.book.dto.AladinResponse;
import com.talk.book.dto.BookSummary;
import com.talk.book.dto.GetBookListResponse;
import com.talk.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {

    private final BookRepository bookRepository;
    private final AladinApiProperties aladinApiProperties;

    public void getAladinList(String category) {

        // Todo: restTemplate 학습 후 수정하기
        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        HttpEntity request = new HttpEntity(httpHeaders);
        ResponseEntity request = new ResponseEntity(HttpStatus.OK);

        String apiKey = aladinApiProperties.getApiKey();
        String baseUrl = aladinApiProperties.getBaseUrl();

        try {
            ResponseEntity<AladinResponse> response = restTemplate.exchange(
                    baseUrl + "?ttbkey=" + apiKey + "&QueryType=" + category + "&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101",
                    HttpMethod.GET,
                    request,
                    AladinResponse.class
            );

            log.info("Successfully received Aladin response.");

            // todo: 받아온 리스트 책에 저장하기
            List<BookSummary> item = response.getBody().getItem();
            for (BookSummary bookSummary : item) {
                Book book = new Book();
                book.setTitle(bookSummary.getTitle());
                book.setIsbn13(bookSummary.getIsbn13());
                book.setCover(bookSummary.getCover());

                // todo: 변경 여지 있는지 확인
                if (category.equals("BestSeller")) {
                    book.setCategory(BookCategory.Bestseller);
                } else if (category.equals("BlogBest")) {
                    book.setCategory(BookCategory.BlogBest);
                } else {
                    book.setCategory(BookCategory.ItemNewAll);
                }
                bookRepository.save(book);
            }

        } catch (RestClientException e) {
            log.error("RestTemplate call failed: {}", e.getMessage(), e);

            try {
                ResponseEntity<String> errorResponse = restTemplate.exchange(
                        baseUrl + "?ttbkey=" + apiKey + "&QueryType=" + category + "&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101",
                        HttpMethod.GET,
                        request,
                        String.class
                );
                log.error("Aladin API returned non-JSON response (HTML/XML?). Status: {}, Body: {}",
                        errorResponse.getStatusCode(), errorResponse.getBody());
            } catch (RestClientException innerEx) {
                log.error("Failed to retrieve error response body as String: {}", innerEx.getMessage());
            }
        }

    }

    public GetBookListResponse getBookList(String category) {

        List<Book> books = bookRepository.findByCategory(BookCategory.valueOf(category)).orElseThrow();
        List<BookSummary> bookSummaries = new ArrayList<>();
        for (Book book : books) {
            BookSummary bookSummary = new BookSummary(book.getTitle(), book.getIsbn13(), book.getCover());
            bookSummaries.add(bookSummary);
        }

        return new GetBookListResponse(BookCategory.valueOf(category), bookSummaries);
    }
}
