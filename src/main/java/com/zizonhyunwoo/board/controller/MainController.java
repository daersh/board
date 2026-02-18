package com.zizonhyunwoo.board.controller;

import com.zizonhyunwoo.board.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final JwtUtil jwtUtil;


    @GetMapping("/index")
    public String index(
            @CookieValue(name = "accessToken", required = false) String token,
            Model model) {

        // 1. 토큰이 없으면 로그인 페이지로 리다이렉트
        if (token == null || token.isEmpty()) {
            return "redirect:/login-page"; // 실제 로그인 페이지 경로로 수정하세요
        }

         jwtUtil.validateToken(token);

        // 3. 환영 메시지 전달
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("message", "환영합니다! 회원님.");
        return "index";
    }
}