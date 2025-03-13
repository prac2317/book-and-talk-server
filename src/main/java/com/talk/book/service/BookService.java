package com.talk.book.service;

import com.talk.book.domain.Book;
import com.talk.book.domain.BookType;
import com.talk.book.dto.AladinResponse;
import com.talk.book.dto.BookResponse;
import com.talk.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public void getAladinList(String category) {

        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        HttpEntity request = new HttpEntity(httpHeaders);
        ResponseEntity request = new ResponseEntity(HttpStatus.OK);

        String baseUrl = "http://www.aladin.co.kr/ttb/api/ItemList.aspx";
        String apiKey = "apiKey 삭제함";

        ResponseEntity<AladinResponse> response = restTemplate.exchange(
                baseUrl + "?ttbkey=" + apiKey + "&QueryType=" + category + "&MaxResults=10&start=1&SearchTarget=Book&output=js&Version=20131101",
                HttpMethod.GET,
                request,
                AladinResponse.class
        );

        // todo: 받아온 리스트 책에 저장하기
        List<BookResponse> item = response.getBody().getItem();
        for (BookResponse bookResponse : item) {
            Book book = new Book();
            book.setTitle(bookResponse.getTitle());
            book.setIsbn13(bookResponse.getIsbn13());
            book.setCover(bookResponse.getCover());
            if (category == "BestSeller") {
                book.setType(BookType.Bestseller);
            } else if (category.equals("BlogBest")) {
                book.setType(BookType.BlogBest);
            } else {
                book.setType(BookType.ItemNewAll);
            }
            bookRepository.save(book);
        }
    }
}
