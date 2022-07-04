package com.board_test.demo.dto;

import com.board_test.demo.domain.FileEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;

@Data
@ToString
@NoArgsConstructor
public class FileDto {

    private Long id;
    private String origFileName;
    private String fileName;
    private String filePath;

    public FileEntity toEntity(){
        FileEntity build = FileEntity.builder()
                .id(id)
                .origFileName(getOrigFileName())
                .fileName(getFileName())
                .filePath(getFilePath())
                .build();

        return build;
    }

    @Builder

    public FileDto(Long id, String origFileName, String fileName, String filePath) {
        this.id = id;
        this.origFileName = origFileName;
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
