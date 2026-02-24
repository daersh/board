package com.zizonhyunwoo.board.api;

import com.zizonhyunwoo.board.dao.UserRepository;
import com.zizonhyunwoo.board.dto.LoginRequest;
import com.zizonhyunwoo.board.model.UserEntity;
import com.zizonhyunwoo.board.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @PostMapping("")
    @Transactional
    public ResponseEntity<Void> login(@RequestBody@Valid LoginRequest dto, HttpServletResponse response) {
        log.info("Login Request: {}", dto);
        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("비밀번호 틀림");

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                CreateCookie("accessToken",jwtUtil.createToken(user),3600).toString()
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        log.info("Logout Request: 쿠키 삭제를 요청합니다.");

        // 기존 쿠키와 동일한 설정(이름, 경로 등)을 가지고 만료 시간만 0으로 설정
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                CreateCookie("accessToken",null,0).toString()
        );

        return ResponseEntity.ok().build();
    }

    private ResponseCookie CreateCookie(String name, String token,int maxAge){
        return ResponseCookie.from(name, token)
                .httpOnly(true)         // js에서 읽기 불가 http 요청에서만 쿠키를 담을 수 있다
                .secure(false)          // HTTPS(보안 연결)에서만 쿠키를 보낼지 결정
                .path("/")
                .maxAge(maxAge)
                .sameSite("Lax")     // csrf
                .build();
    }
}
