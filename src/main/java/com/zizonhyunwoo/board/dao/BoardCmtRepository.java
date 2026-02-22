package com.zizonhyunwoo.board.dao;

import com.zizonhyunwoo.board.model.BoardCmtEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardCmtRepository extends JpaRepository<BoardCmtEntity, UUID> {

}
