package com.fileupload.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String saveFile(MultipartFile file);

    String deleteFile(String filePath);
}
