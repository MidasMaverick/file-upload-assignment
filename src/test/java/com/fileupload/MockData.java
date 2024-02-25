package com.fileupload;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class MockData {
    public static MockMultipartFile mockMultipartFile(byte[] content) {
        return new MockMultipartFile("file", "test.pdf", MediaType.APPLICATION_PDF_VALUE, content);
    }
}
