package com.talk.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetBookListResponse {

    private int totalCount;
    private List<AladinResponse> data;
}
