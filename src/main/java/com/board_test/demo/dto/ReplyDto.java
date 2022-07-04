package com.board_test.demo.dto;

import com.board_test.demo.domain.Board;
import com.board_test.demo.domain.Reply;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString
public class ReplyDto {

    private Long id;
    private String content;
    private Long parent;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public Reply toEntity(){
        Reply reply = Reply.builder()
                .id(id)
                .content(content)
                .build();
        return reply;
    }

    @Builder
    public ReplyDto(Long id, String content, LocalDateTime createdDate, LocalDateTime modifiedDate, Board board) {
        this.id = id;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
