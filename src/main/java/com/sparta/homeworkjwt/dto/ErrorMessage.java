package com.sparta.homeworkjwt.dto;

import lombok.Data;

@Data
public class ErrorMessage{
    private String statusCode;
    private String msg;
    public ErrorMessage(String error, String msg) {
        this.statusCode = error;
        this.msg = msg;
    }
}
