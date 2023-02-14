package com.sparta.homeworkjwt.service;


import com.sparta.homeworkjwt.component.JwtUtil;
import com.sparta.homeworkjwt.entity.User;
import com.sparta.homeworkjwt.entity.UserRoleEnum;
import com.sparta.homeworkjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public String signup(String username, String password) {
        final String PASS_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,15}$";//소문자 1, 대문자1, 숫자1, 특수문자1개 포함 8자 이상 15자 이하
        final String NAME_PATTERN = "^[a-z0-9]{4,10}$";// 소문자, 숫자 포함 4자 이상 10자 이하

        Pattern passPattern = Pattern.compile(PASS_PATTERN);
        Matcher passMatcher = passPattern.matcher(password);
        Pattern namePattern = Pattern.compile(NAME_PATTERN);
        Matcher nameMatcher = namePattern.matcher(username);

        if(nameMatcher.matches() && passMatcher.matches()) { //아이디, 패스워드 조건 만족할 때 실행
            Optional<User> optionalUser = userRepository.findByUsername(username);

            if(optionalUser.isPresent()) { // 중복체크
                throw new IllegalArgumentException("이미 존재하는 username 입니다.");
            }

            User user = new User(username, password, UserRoleEnum.USER);
            userRepository.save(user);
            return "회원가입 성공";

        }

        return "회원가입 실패";
    }

    public String login(String username, String password) {

        User user = userRepository.findByUsername(username).orElseThrow( () -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        if(!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.createToken(user.getUsername(), user.getRole());
    }
}
