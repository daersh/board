package com.zizonhyunwoo.board.dao;

import aj.org.objectweb.asm.commons.Remapper;
import com.zizonhyunwoo.board.model.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BoardRepository extends JpaRepository<BoardEntity, UUID> {

//    @Query("SELECT b from BoardEntity b  join fetch b.user")
    @EntityGraph(attributePaths = {"user"})
    Page<BoardEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    @Query("select b from BoardEntity b where b.status = :status")
    Page<BoardEntity> findAllByStatus(int status, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Optional<BoardEntity> findById(UUID id);

}
