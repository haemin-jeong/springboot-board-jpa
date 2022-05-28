package com.prgrms.board.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static javax.servlet.http.HttpServletResponse.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleIllegalArgumentException(NotFoundException e) {
        return ErrorResponse.of(SC_NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ErrorResponse.of(SC_METHOD_NOT_ALLOWED, "허용되지 않은 Method입니다.");
    }

    /**
     * @Valid, @Validated 검증 실패시 발생한다.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ErrorResponse.of(SC_BAD_REQUEST, "입력 값이 잘못되었습니다.", FieldErrors.from(e.getFieldErrors()));
    }

    /**
     * HttpMessageConverter에서 read를 실패한 경우에 발생한다.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ErrorResponse.of(SC_BAD_REQUEST, "요청 바디의 형식이 잘못되었습니다.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleUnexpectedException(Exception e) {
        log.error("unexpected error  = {}", e);
        return ErrorResponse.of(SC_INTERNAL_SERVER_ERROR, "예상치 못한 문제가 발생하였습니다.");
    }
}
