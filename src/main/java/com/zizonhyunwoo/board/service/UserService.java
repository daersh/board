package com.zizonhyunwoo.board.service;

import com.zizonhyunwoo.board.config.UserPrincipal;
import com.zizonhyunwoo.board.dao.UserRepository;
import com.zizonhyunwoo.board.model.UserEntity;
import com.zizonhyunwoo.board.request.UserRequest;
import com.zizonhyunwoo.board.response.PageResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PageResponse<UserEntity> findAll(@RequestParam int page) {

        return PageResponse.of(userRepository.findAll(PageRequest.of(page, 10, Sort.by("id").descending())));
    }

    public ResponseEntity<UserEntity> info(@AuthenticationPrincipal UserPrincipal user) {
        System.out.println(user);
        UUID id = user.getUserId();

        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public UserEntity save(@RequestBody @Valid UserRequest.Create request) throws BadRequestException {

        Optional<UserEntity> user =userRepository.findByEmail(request.getEmail());

        if (user.isPresent())
            throw new BadRequestException("이메일 중복 ");

        return userRepository.save(new UserEntity(request,passwordEncoder.encode(request.getPassword())));

    }

}
