package com.zizonhyunwoo.board.dao;

import com.zizonhyunwoo.board.model.BoardCmtEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BoardCmtRepository extends JpaRepository<BoardCmtEntity, UUID> {

    @EntityGraph(attributePaths = {"user"})
    List<BoardCmtEntity> findAllByBoard_Id(UUID boardId, Pageable pageable);
}
