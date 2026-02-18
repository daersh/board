package com.zizonhyunwoo.board.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardEntity extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;
    @Column
    private int status;
    @Column
    private int reportCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
}
