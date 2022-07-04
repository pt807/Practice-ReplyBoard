package com.board_test.demo.repository;

import com.board_test.demo.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

//    @Query(nativeQuery = true, value = "select * from reply order by if(isnull(parent_id), reply_id, parent_id)")
//    List<Reply> findByBoardId(Long boardId);


    /**
     * 매개변수로 받아온 boardId를 외래키로 가지는 댓글들을 리턴한다.
     * WHERE 조건으로 parent 속성이 null(=댓글)인 값만 조회한다.
     * ORDER BY 조건으로 id의 오름차순(=댓글 등록순)으로 정렬한다.
     *
     * @param boardId
     * @return boardId를 외래키로 가지는 댓글들
     */
    List<Reply> findByBoardIdAndParentIsNullOrderByIdAsc(Long boardId);
}
