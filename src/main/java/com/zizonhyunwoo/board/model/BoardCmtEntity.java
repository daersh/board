package com.zizonhyunwoo.board.model;

import com.zizonhyunwoo.board.dto.BoardCmtDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "board_comment")
public class BoardCmtEntity extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentType type;

    @Column
    private int status;

    @Column
    private int reportCount;

    @Column(nullable = false)
    private String targetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity board;

    public void create(BoardCmtDto.Create create, BoardEntity board, UserEntity user) {
        this.content = create.getContent();
        this.type = create.getType();
        this.targetId = create.getTargetId();
        this.status = 0;
        this.reportCount = 0;
        this.user = user;
        this.board = board;
    }

}
