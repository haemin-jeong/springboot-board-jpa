package com.prgrms.board.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ApiResponse<T> {

    int statusCode;
    T data;

    public static <T> ApiResponse<T> of(int statusCode, T data) {
        return new ApiResponse<>(statusCode, data);
    }

}
