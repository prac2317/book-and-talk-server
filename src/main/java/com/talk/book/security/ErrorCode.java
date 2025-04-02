package com.talk.book.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    MEMBER_NOT_FOUND("회원 정보를 찾을 수 없습니다."),
    CLUB_NOT_FOUND("클럽 정보를 찾을 수 없습니다."),
    FAVORITE_NOT_FOUND("즐겨찾기 정보를 찾을 수 없습니다."),
    ALREADY_FAVORITED("이미 즐겨찾기에 추가된 클럽입니다.");

    private final String message;
}