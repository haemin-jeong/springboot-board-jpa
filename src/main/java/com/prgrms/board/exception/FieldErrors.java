package com.prgrms.board.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class FieldErrors {

    private final List<FieldErrorResponse> errors;

    public static FieldErrors from(List<FieldError> errors) {
        List<FieldErrorResponse> errorResponses = errors.stream()
                .map(FieldErrorResponse::from)
                .collect(Collectors.toList());
        return new FieldErrors(errorResponses);
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    private static class FieldErrorResponse {
        private String name;
        private String value;
        private String message;

        private static FieldErrorResponse from(FieldError fieldError) {
            return new FieldErrorResponse(
                    fieldError.getField(),
                    String.valueOf(fieldError.getRejectedValue()),
                    fieldError.getDefaultMessage()
            );
        }
    }

}
