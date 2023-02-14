package com.sparta.homeworkjwt.handler;

import com.sparta.homeworkjwt.dto.ErrorMessage;
import com.sparta.homeworkjwt.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public  ResponseDto<?> handleException(Exception ex) {
        return ResponseDto.fail(500, ex.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<?> handle(Exception ex) {
        return ResponseDto.fail(400, ex.getMessage());
    }
}
