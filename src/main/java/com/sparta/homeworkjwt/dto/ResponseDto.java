package com.sparta.homeworkjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
//@Builder
public class ResponseDto<T> {
    private int statusCode;
    private T result;

    public ResponseDto (int statusCode, T data) {
        this.statusCode = statusCode;
        this.result = data;
    }

    public static <T> ResponseDto<T> success(T result) {
        return new ResponseDto<>(200, result);
    }

    public static <T> ResponseDto<T> fail(int statusCode, T result) {
        return new ResponseDto<>(statusCode,result);
    }
}
