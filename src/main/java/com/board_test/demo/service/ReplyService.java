package com.board_test.demo.service;

import com.board_test.demo.domain.Board;
import com.board_test.demo.domain.Reply;
import com.board_test.demo.dto.ReplyDto;
import com.board_test.demo.repository.BoardRepository;
import com.board_test.demo.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void saveReply(Long boardId, ReplyDto replyDto){
        Board board = boardRepository.findById(boardId).get();

        Reply reply = replyDto.toEntity();
        reply.setBoard(board);
        if (replyDto.getParent() != null) {
            Reply parentReply = replyRepository.findById(replyDto.getParent()).get();
            reply.setParent(parentReply);
        }

        replyRepository.save(reply);
    }

    @Transactional
    public void updateReply(Long id, String content){
        Reply reply = replyRepository.findById(id).get();
        reply.updateContent(content);
    }

    @Transactional
    public void deleteReply(Long replyId){
        replyRepository.deleteById(replyId);
    }

    public List<Reply> getReplyList(Long id){
        List<Reply> replyList = replyRepository.findByBoardIdAndParentIsNullOrderByIdAsc(id);

        return replyList;
    }
}
