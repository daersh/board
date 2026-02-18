package com.zizonhyunwoo.board.dao;

import com.zizonhyunwoo.board.model.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends JpaRepository<BoardEntity, UUID> {
}
