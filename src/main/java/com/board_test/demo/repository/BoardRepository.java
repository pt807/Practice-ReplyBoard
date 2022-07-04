package com.board_test.demo.repository;

import com.board_test.demo.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByTitleContaining(String title, Pageable pageable);
    Page<Board> findByTitleContainingOrContent(String title, String content, Pageable pageable);
}
