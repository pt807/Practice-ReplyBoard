package com.board_test.demo.service;

import com.board_test.demo.domain.Board;
import com.board_test.demo.dto.BoardDto;
import com.board_test.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public void savePost(BoardDto boardDto){
        boardRepository.save(boardDto.toEntity());
    }

    //업데이트는 저장이랑 같이하면 안됨 영속성 꺠짐
    @Transactional
    public void editPost(BoardDto boardDto){
        Board board = boardRepository.findById(boardDto.getId()).get();
        board.updatePost(boardDto);
    }

    @Transactional
    public void deletePost(Long id){
        boardRepository.deleteById(id);
    }

    public BoardDto getPost(Long id){
        Board board = boardRepository.findById(id).get();
        BoardDto boardDto = BoardDto.builder()
                .id(board.getId())
                .writer(board.getWriter())
                .title(board.getTitle())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .fileId(board.getFileId())
                .build();
        return boardDto;
    }

    public Page<Board> boardPage(Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    public Page<Board> search(String searchField, String searchText, Pageable pageable){
        if(searchField.equals("title")){
            return boardRepository.findByTitleContaining(searchText, pageable);
        }else if(searchField.equals("title2")){
            return  boardRepository.findByTitleContainingOrContent(searchText, searchText, pageable);
        }
        return boardPage(pageable);
    }
}
