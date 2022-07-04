package com.board_test.demo.dto;

import com.board_test.demo.domain.Board;
import com.board_test.demo.domain.Reply;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BoardDto {

    private Long id;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long fileId;

    public Board toEntity(){
        Board board = Board.builder()
                .id(id)
                .title(title)
                .writer(writer)
                .content(content)
                .fileId(fileId)
                .build();
        return board;
    }

    @Builder
    public BoardDto(Long id, String title, String writer, String content, LocalDateTime createdDate, LocalDateTime modifiedDate, Long fileId) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.content = content;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.fileId = fileId;
    }
}
