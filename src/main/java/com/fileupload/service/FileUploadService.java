package com.fileupload.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

    private final FileStorageService fileStorageService;
    private final NotificationService notificationService;

    public FileUploadService(@Qualifier("localStorageService") FileStorageService fileStorageService, @Qualifier("emailNotificationService") NotificationService notificationService) {
        this.fileStorageService = fileStorageService;
        this.notificationService = notificationService;
    }

    public String uploadFile(MultipartFile file, String sendTo) {
        String filePath = fileStorageService.saveFile(file);
        notificationService.send(sendTo, "File uploaded successfully: ".concat(filePath));
        return filePath;
    }
}
