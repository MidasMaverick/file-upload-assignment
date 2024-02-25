package com.fileupload.service;

import com.fileupload.MockData;
import com.fileupload.exception.FUException;
import com.fileupload.model.ResultCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileUploadServiceTest {

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private FileUploadService fileUploadService;


    @Test
    @DisplayName("Test upload file - success")
    void testUploadFile_success() {
        MockMultipartFile mockMultipartFile = MockData.mockMultipartFile("test".getBytes());
        when(fileStorageService.saveFile(mockMultipartFile)).thenReturn("/dir/test.pdf");
        doNothing().when(notificationService).send(eq("test@mail.com"), anyString());
        String filePath = fileUploadService.uploadFile(mockMultipartFile, "test@mail.com");

        assertEquals("/dir/test.pdf", filePath);

        verify(fileStorageService, times(1)).saveFile(mockMultipartFile);
        verify(notificationService, times(1)).send(eq("test@mail.com"), eq("File uploaded successfully: ".concat(filePath)));
    }

    @Test
    @DisplayName("Test upload file an error from file storage service - success")
    void testUploadFileAnErrorFromFileStorageService_fail() {
        MockMultipartFile mockMultipartFile = MockData.mockMultipartFile("test".getBytes());
        when(fileStorageService.saveFile(mockMultipartFile)).thenThrow(new FUException(ResultCode.UNABLE_TO_PROCESS));

        FUException exception = assertThrows(FUException.class, () -> fileUploadService.uploadFile(mockMultipartFile, "test@mail.com"));

        assertEquals(ResultCode.UNABLE_TO_PROCESS, exception.getResultCode());

        verify(fileStorageService, times(1)).saveFile(mockMultipartFile);
        verify(notificationService, never()).send(anyString(), anyString());
    }

    @Test
    @DisplayName("Test upload file an error from notification service - success")
    void testUploadFileAnErrorFromNotificationService_fail() {
        MockMultipartFile mockMultipartFile = MockData.mockMultipartFile("test".getBytes());
        when(fileStorageService.saveFile(mockMultipartFile)).thenReturn("/dir/test.pdf");
        doThrow(new FUException(ResultCode.UNABLE_TO_PROCESS)).when(notificationService).send(eq("test@mail.com"), anyString());

        FUException exception = assertThrows(FUException.class, () -> fileUploadService.uploadFile(mockMultipartFile, "test@mail.com"));

        assertEquals(ResultCode.UNABLE_TO_PROCESS, exception.getResultCode());

        verify(fileStorageService, times(1)).saveFile(mockMultipartFile);
        verify(notificationService, times(1)).send(eq("test@mail.com"), anyString());
    }
}