package com.fileupload.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class FileUploadResponse {

    private String filePath;

    public FileUploadResponse(String filePath) {
        this.filePath = filePath;
    }
}
