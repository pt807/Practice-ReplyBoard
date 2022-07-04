package com.board_test.demo.service;

import com.board_test.demo.domain.FileEntity;
import com.board_test.demo.dto.FileDto;
import com.board_test.demo.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FileService {
    private FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    public Long fileSave(FileDto fileDto) {
        return fileRepository.save(fileDto.toEntity()).getId();
    }

    public FileDto getFile(Long id) {
        FileEntity fileEntity = fileRepository.findById(id).get();

        FileDto fileDto = FileDto.builder()
                .id(fileEntity.getId())
                .origFileName(fileEntity.getOrigFileName())
                .fileName(fileEntity.getFileName())
                .filePath(fileEntity.getFilePath())
                .build();

        return fileDto;
    }
}
