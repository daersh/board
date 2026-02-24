package com.zizonhyunwoo.board.config;

import lombok.Getter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

@Getter
@ToString
public class UserPrincipal extends User {

    private final UUID userId;
    private final String email;
    private final String nickname;

    public UserPrincipal(String username, @Nullable String password, Collection<? extends GrantedAuthority> authorities, String userId, String email, String nickname) {
        super(username, password, authorities);
        this.userId = UUID.fromString(userId);
        this.email = email;
        this.nickname = nickname;
    }


}
