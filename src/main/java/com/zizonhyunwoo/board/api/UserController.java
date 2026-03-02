package com.zizonhyunwoo.board.api;

import com.zizonhyunwoo.board.config.UserPrincipal;
import com.zizonhyunwoo.board.dto.UserDto;
import com.zizonhyunwoo.board.model.UserEntity;
import com.zizonhyunwoo.board.dto.PageDto;
import com.zizonhyunwoo.board.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public PageDto<UserEntity> findAll(@RequestParam int page) {

        return userService.findAll(page);
    }

    @GetMapping("/info")
    public ResponseEntity<UserEntity> info(@AuthenticationPrincipal UserPrincipal user) {
        return userService.info(user);
    }

    @PostMapping("")
    @Transactional
    public UserEntity save(@RequestBody@Valid UserDto.Create request) throws BadRequestException {
        return userService.save(request);
    }


}
