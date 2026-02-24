package com.zizonhyunwoo.board.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequest {

    @Getter
    @NoArgsConstructor()
    @AllArgsConstructor()
    public static class Create{

        @NotBlank
        String username;
        @NotBlank
        String email;
        @NotBlank
        String password;
        @NotBlank
        String nickname;
        @NotBlank
        String phone;

    }
}
