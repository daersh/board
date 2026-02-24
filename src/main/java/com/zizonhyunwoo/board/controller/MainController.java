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

        if (token == null || token.isEmpty() || !jwtUtil.validateToken(token)) {
            System.out.println("invalid token");
            return "redirect:/login";
        }
        System.out.println("valid token");

        model.addAttribute("isLoggedIn", true);
        return "index";
    }
}