package com.fileupload.service.impl;

import com.fileupload.exception.FUException;
import com.fileupload.model.ResultCode;
import com.fileupload.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@Qualifier("localStorageService")
public class LocalStorageServiceImpl implements FileStorageService {

    @Value("${upload.dir}")
    private String uploadDir;
    @Override
    public String saveFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        String storedFileName = UUID.randomUUID().toString().concat(fileExtension);

        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            if (!uploadPath.toFile().exists()) {
                uploadPath.toFile().mkdirs();
            }
            try (FileOutputStream fos = new FileOutputStream(uploadPath.resolve(storedFileName).toFile())) {
                fos.write(file.getBytes());
            }
        } catch (Exception ex) {
            log.error("Failed to store file: {}", storedFileName, ex);
            throw new FUException(ResultCode.UNABLE_TO_PROCESS);
        }
        return Paths.get(uploadDir, storedFileName).toString();
    }

    @Override
    public String deleteFile(String filePath) {
        return null;
    }
}
