package com.talk.book.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String nickname;
    private String password;
}
