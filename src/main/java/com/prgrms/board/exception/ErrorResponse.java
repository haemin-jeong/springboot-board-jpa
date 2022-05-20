package com.prgrms.board.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private String errorMessage;
    private List<FieldError> errors;

    public static ErrorResponse of(String errorMessage) {
        return new ErrorResponse(errorMessage, new ArrayList<>());
    }

    public static ErrorResponse of(String errorMessage, List<org.springframework.validation.FieldError> errors) {
        List<FieldError> fieldErrors = FieldError.of(errors);
        return new ErrorResponse(errorMessage, fieldErrors);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    static class FieldError {
        private String name;
        private String value;
        private String message;

        private static FieldError of(String name, String value, String message) {
            return new FieldError(name, value, message);
        }

        private static List<FieldError> of(List<org.springframework.validation.FieldError> errors) {
            return errors.stream().map(error -> FieldError.of(
                        error.getField(),
                        error.getRejectedValue() == null ? "null" : String.valueOf(error.getRejectedValue()),
                        error.getDefaultMessage())
                    ).collect(Collectors.toList());
        }
    }
}
