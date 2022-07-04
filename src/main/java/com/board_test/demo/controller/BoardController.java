package com.board_test.demo.controller;

import com.board_test.demo.domain.Board;
import com.board_test.demo.dto.BoardDto;
import com.board_test.demo.dto.FileDto;
import com.board_test.demo.service.BoardService;
import com.board_test.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

@Controller
@RequiredArgsConstructor  //final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;

    @GetMapping("/")
    public String list(Model model,
                       @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchField,
                       @RequestParam(required = false, defaultValue = "") String searchText) {

        Page<Board> list = boardService.boardPage(pageable);

        if (searchText != null) {
            list = boardService.search(searchField, searchText, pageable);
        }

        int cnt = (int) list.getTotalElements();
        int totalPage = Math.max(1, list.getTotalPages());
        int nowPage = list.getPageable().getPageNumber() + 1; // == pageable.getPageNumber 현재페이지 가져오기
        int startPage = Math.max(1, nowPage - 5); //((nowPage)/pageBlock) * pageBlock + 1;
        int endPage = Math.min(totalPage, nowPage + 4); //startPage + pageBlock - 1;
        if (endPage > totalPage) endPage = totalPage;  // endPage= totalPage<endPage? totalPage:endPage;

        model.addAttribute("cnt", cnt);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("searchField", searchField);

        model.addAttribute("postList", list);
        return "list";
    }

    @GetMapping("/post")
    public String write() {
        return "write";
    }

    @PostMapping("/post")
    public String write(@RequestParam("file") List<MultipartFile> files, BoardDto boardDto) {

        String savePath = System.getProperty("user.dir") + "\\files";
        String origFilename = "";
        String filename = "";
        String filePath = "";
        if (!new File(savePath).exists()) {
            try {
                new File(savePath).mkdir();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        List<Map<String, String>> fileList = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            origFilename = files.get(i).getOriginalFilename();
            filename = UUID.randomUUID() + origFilename;

            Map<String, String> map = new HashMap<>();
            map.put("origFilename", origFilename);
            map.put("filename", filename);

            fileList.add(map);
        }

        try {
            for (int i = 0; i < files.size(); i++) {
                filePath = savePath + "\\" + fileList.get(i).get("filename");
                files.get(i).transferTo(new File(filePath));
            }

            System.out.println("다중 파일 업로드 성공!");

        } catch (IllegalStateException | IOException e) {
            System.out.println("다중 파일 업로드 실패");
            // 만약 업로드 실패하면 파일 삭제
            for (int i = 0; i < files.size(); i++) {
                new File(savePath + "\\" + fileList.get(i).get("filename")).delete();
            }
        }

        FileDto fileDto = new FileDto();
        for (int i = 0; i < fileList.size(); i++) {
            fileDto.setOrigFileName(fileList.get(i).get("origFilename"));
            fileDto.setFileName(fileList.get(i).get("filename"));
            fileDto.setFilePath(filePath);
            Long fileId = fileService.fileSave(fileDto);
            boardDto.setFileId(fileId);

        }
        boardService.savePost(boardDto);
        return "redirect:/";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);

        return "view";
    }

    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "edit";
    }

    @PostMapping("/post/edit/{id}")
    public String update(BoardDto boardDto) {
        boardService.editPost(boardDto);
        return "redirect:/";
    }

    @PostMapping("/post/delete/{id}")
    public String deletePost(@PathVariable("id") Long id) {
        boardService.deletePost(id);
        return "redirect:/";
    }
}