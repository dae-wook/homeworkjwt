package com.sparta.homeworkjwt.controller;

import com.sparta.homeworkjwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(String username, String password) {

        return userService.signup(username, password);
    }

    @PostMapping("login")
    public String login(String username, String password) {
        return userService.login(username, password);
    }



}
