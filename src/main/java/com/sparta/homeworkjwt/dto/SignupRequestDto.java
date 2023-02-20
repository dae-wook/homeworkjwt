package com.sparta.homeworkjwt.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class SignupRequestDto {

    private String username;
    private String password;

    final String PASS_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,15}$";//소문자 1, 대문자1, 숫자1, 특수문자1개 포함 8자 이상 15자 이하
    final String NAME_PATTERN = "^[a-z0-9]{4,10}$";// 소문자, 숫자 포함 4자 이상 10자 이하

    public boolean isValid() {
        Pattern passPattern = Pattern.compile(PASS_PATTERN);
        Matcher passMatcher = passPattern.matcher(password);
        Pattern namePattern = Pattern.compile(NAME_PATTERN);
        Matcher nameMatcher = namePattern.matcher(username);
        return nameMatcher.matches() && passMatcher.matches();
    }

}
