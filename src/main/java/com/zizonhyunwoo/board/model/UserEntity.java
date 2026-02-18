package com.zizonhyunwoo.board.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends TimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private String phone;
    @Column
    private String nickname;
    @Column
    private int status;
    @Column
    private String role;

    public UserEntity(UserEntity request, @Nullable String encode) {
        this.username = request.getUsername();
        this.password = encode;
        this.email = request.getEmail();
        this.phone = request.getPhone();
        this.nickname = request.getNickname();
        this.status = 0;
    }

}
