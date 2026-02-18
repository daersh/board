package com.zizonhyunwoo.board.api;

import com.zizonhyunwoo.board.dao.UserRepository;
import com.zizonhyunwoo.board.dto.LoginRequest;
import com.zizonhyunwoo.board.model.UserEntity;
import com.zizonhyunwoo.board.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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
    public ResponseEntity<Void> login(@RequestBody LoginRequest dto, HttpServletResponse response) {
        log.info("Login Request: {}", dto);
        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new IllegalArgumentException("비밀번호 틀림");

        String token = jwtUtil.createToken(user);
        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)    // 자바스크립트 접근 방지
                .secure(false)     // HTTPS에서만 전송 (로컬 테스트시 false, 배포시 true)
                .path("/")         // 모든 경로에서 쿠키 사용
                .maxAge(3600)      // 쿠키 유효 시간 (초 단위, 예: 1시간)
                .sameSite("Strict") // CSRF 공격 방지
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        log.info("Logout Request: 쿠키 삭제를 요청합니다.");

        // 기존 쿠키와 동일한 설정(이름, 경로 등)을 가지고 만료 시간만 0으로 설정합니다.
        ResponseCookie cookie = ResponseCookie.from("accessToken", null)
                .httpOnly(true)
                .secure(false) // 배포 시 true로 변경 권장
                .path("/")
                .maxAge(0)     // 핵심: 즉시 만료
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok().build();
    }
}
