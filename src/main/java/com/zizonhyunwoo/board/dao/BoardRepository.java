package com.zizonhyunwoo.board.dao;

import com.zizonhyunwoo.board.model.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<BoardEntity, UUID> {

//    @Query("SELECT b from BoardEntity b  join fetch b.user")
    @EntityGraph(attributePaths = {"user"})
    Page<BoardEntity> findAll(Pageable pageable);
}
