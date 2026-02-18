package com.zizonhyunwoo.board.api;

import com.zizonhyunwoo.board.dao.UserRepository;
import com.zizonhyunwoo.board.dto.LoginRequest;
import com.zizonhyunwoo.board.model.UserEntity;
import com.zizonhyunwoo.board.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("")
    public Page<UserEntity> findAll(@RequestParam int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return userRepository.findAll(pageable);
    }

    @PostMapping("")
    @Transactional
    public UserEntity save(@RequestBody UserEntity request) {
        Optional<UserEntity> user =userRepository.findByEmail(request.getEmail());
        if (user.isEmpty())
            return userRepository.save(new UserEntity(request,passwordEncoder.encode(request.getPassword())));
        return null;
    }

}
