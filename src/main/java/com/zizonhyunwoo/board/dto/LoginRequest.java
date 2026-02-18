package com.zizonhyunwoo.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter@NoArgsConstructor@AllArgsConstructor@ToString
public class LoginRequest {
    private String email;
    private String password;
}
