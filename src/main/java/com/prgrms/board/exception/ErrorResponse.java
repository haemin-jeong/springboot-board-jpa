package com.prgrms.board.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private int statusCode;
    private String errorMessage;
    private FieldErrors fieldErrors;

    public static ErrorResponse of(int statusCode, String errorMessage) {
        return new ErrorResponse(statusCode, errorMessage, null);
    }

    public static ErrorResponse of(int statusCode,String errorMessage, FieldErrors errors) {
        return new ErrorResponse(statusCode, errorMessage, errors);
    }
}
