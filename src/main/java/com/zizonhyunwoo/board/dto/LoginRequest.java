package com.zizonhyunwoo.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter@NoArgsConstructor@AllArgsConstructor@ToString
public class LoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
