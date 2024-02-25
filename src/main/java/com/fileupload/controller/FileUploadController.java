package com.fileupload.controller;

import com.fileupload.annotation.MultipartFileNotEmpty;
import com.fileupload.model.FileUploadResponse;
import com.fileupload.model.ResultCode;
import com.fileupload.model.StandardResponse;
import com.fileupload.service.FileUploadService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@Validated
public class FileUploadController {

    private final FileUploadService fileUploadService;

    @Autowired
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StandardResponse<FileUploadResponse>> uploadFile(@RequestParam("file") @MultipartFileNotEmpty MultipartFile file, @RequestParam("email") @NotBlank String email) {
        String fileName = fileUploadService.uploadFile(file, email);
        return ResponseEntity.ok().body(StandardResponse.createResponse(ResultCode.SUCCESS, new FileUploadResponse(fileName)));
    }
}
