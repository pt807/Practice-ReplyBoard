package com.board_test.demo.controller;

import com.board_test.demo.domain.Reply;
import com.board_test.demo.dto.ReplyDto;
import com.board_test.demo.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/reply/{id}")
    public void saveReply(@PathVariable("id") Long id, @RequestBody ReplyDto replyDto, Model model){
        replyService.saveReply(id, replyDto);
    }

    @DeleteMapping("/reply/delete/{id}")
    public void deleteReply(@PathVariable("id") Long id){
        replyService.deleteReply(id);
    }

    @PutMapping("/reply/edit/{id}")
    public void updateReply(@PathVariable("id") Long id, @RequestBody ReplyDto replyDto) {
        replyService.updateReply(id, replyDto.getContent());
    }

    @GetMapping("/board/{boardId}/replies")
    public List<Reply> getReplies(@PathVariable Long boardId) {
        List<Reply> replies = replyService.getReplyList(boardId);
        return replies;
    }

}
