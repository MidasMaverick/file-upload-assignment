package com.fileupload.service.impl;

import com.fileupload.MockData;
import com.fileupload.exception.FUException;
import com.fileupload.model.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LocalStorageServiceImplTest {

    @InjectMocks
    private LocalStorageServiceImpl localStorageService;

    @TempDir
    File tempDir;

    @BeforeEach
    void before() {
        String path = tempDir.getPath();
        ReflectionTestUtils.setField(localStorageService, "uploadDir", path);
    }

    @Test
    @DisplayName("Test store file - success")
    void testStoreFile_success() {
        MockMultipartFile mockMultipartFile = MockData.mockMultipartFile("test".getBytes());
        String fileOutput = localStorageService.saveFile(mockMultipartFile);
        assertTrue(Files.exists(Paths.get(fileOutput)));
    }

    @Test
    @DisplayName("Test store file with upload dir is null then throw error - Fail")
    void testStoreFile_fail() {
        MockMultipartFile mockMultipartFile = MockData.mockMultipartFile("test".getBytes());
        ReflectionTestUtils.setField(localStorageService, "uploadDir", null);

        FUException exception = assertThrows(FUException.class, () -> localStorageService.saveFile(mockMultipartFile));

        assertEquals(ResultCode.UNABLE_TO_PROCESS, exception.getResultCode());
    }
}