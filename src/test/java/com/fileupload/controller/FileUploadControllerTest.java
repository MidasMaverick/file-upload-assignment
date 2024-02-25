package com.fileupload.controller;

import com.fileupload.MockData;
import com.fileupload.model.ResultCode;
import com.fileupload.service.FileUploadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = FileUploadController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.fileupload.filter.*"))
@AutoConfigureMockMvc(addFilters = false)
class FileUploadControllerTest {

    private static final String UPLOAD_FILE_URI = "/file/upload";

    @MockBean
    private FileUploadService fileUploadService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Test call api /file/upload - success")
    void testCallApiUploadFile_success() throws Exception {
        MockMultipartFile mockMultipartFile = MockData.mockMultipartFile("test".getBytes());
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.put("email", List.of("test@gmail.com"));

        when(fileUploadService.uploadFile(any(MultipartFile.class), eq("test@gmail.com"))).thenReturn("/dir/test.pdf");

        mockMvc.perform(multipart(UPLOAD_FILE_URI)
                        .file(mockMultipartFile)
                        .params(multiValueMap)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.responseCode.code", is(ResultCode.SUCCESS.getCode())))
                .andExpect(jsonPath("$.responseCode.desc", is(ResultCode.SUCCESS.getDesc())))
                .andExpect(jsonPath("$.responseBody.filePath", is("/dir/test.pdf")));

        verify(fileUploadService, times(1)).uploadFile(any(MultipartFile.class), eq("test@gmail.com"));
    }

    @Test
    @DisplayName("Test call api /file/upload invalid request when email is blank - fail")
    void testCallApiUploadFileInvalidRequestEmailIsBlank_fial() throws Exception {
        MockMultipartFile mockMultipartFile = MockData.mockMultipartFile("test".getBytes());

        mockMvc.perform(multipart(UPLOAD_FILE_URI)
                        .file(mockMultipartFile)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.responseCode.code", is(ResultCode.INVALID_REQUEST.getCode())))
                .andExpect(jsonPath("$.responseCode.desc", is(ResultCode.INVALID_REQUEST.getDesc())))
                .andExpect(jsonPath("$.errors", is(List.of("email parameter is missing"))));

        verify(fileUploadService, never()).uploadFile(any(MultipartFile.class), anyString());
    }

    @Test
    @DisplayName("Test call api /file/upload invalid request when the file is not attached - fail")
    void testCallApiUploadFileInvalidRequestTheFileIsNotAttached_fial() throws Exception {
        MockMultipartFile mockMultipartFile = MockData.mockMultipartFile("test".getBytes());
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.put("email", List.of("test@gmail.com"));

        mockMvc.perform(multipart(UPLOAD_FILE_URI)
                        .params(multiValueMap)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.responseCode.code", is(ResultCode.INVALID_REQUEST.getCode())))
                .andExpect(jsonPath("$.responseCode.desc", is(ResultCode.INVALID_REQUEST.getDesc())))
                .andExpect(jsonPath("$.errors", is(List.of("file parameter is missing"))));

        verify(fileUploadService, never()).uploadFile(any(MultipartFile.class), anyString());
    }

}