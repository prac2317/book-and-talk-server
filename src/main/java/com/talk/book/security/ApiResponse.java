package com.talk.book.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse {
    private String message;

    public static ApiResponse ok(String message) {
        return new ApiResponse(message);
    }
}
