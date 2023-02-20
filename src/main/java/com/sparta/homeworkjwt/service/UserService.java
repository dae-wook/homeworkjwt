package com.sparta.homeworkjwt.service;


import com.sparta.homeworkjwt.component.JwtUtil;
import com.sparta.homeworkjwt.dto.ResponseDto;
import com.sparta.homeworkjwt.dto.SignupRequestDto;
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
    public ResponseDto<String> signup(SignupRequestDto requestDto) {

        if(requestDto.isValid()) { //아이디, 패스워드 조건 만족할 때 실행
            Optional<User> optionalUser = userRepository.findByUsername(requestDto.getUsername());

            if (optionalUser.isPresent()) { // 중복체크
                throw new IllegalArgumentException("중복된 username 입니다.");
            }

            User user = new User(requestDto.getUsername(), requestDto.getPassword(), UserRoleEnum.USER);
            userRepository.save(user);
            return ResponseDto.success("회원가입 성공");

        }else {
            throw new IllegalArgumentException("아이디 / 비밀번호 형식이 맞지 않습니다");
        }
    }

    public ResponseDto<String> login(String username, String password) {

        User user = userRepository.findByUsername(username).orElseThrow( () -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        if(!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return ResponseDto.success(jwtUtil.createToken(user.getUsername(), user.getRole()));
    }
}
