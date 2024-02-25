package com.fileupload.service.impl;

import com.fileupload.exception.FUException;
import com.fileupload.model.ResultCode;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailNotificationServiceImplTest {
    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailNotificationServiceImpl emailNotificationService;

    @Test
    @DisplayName("Test send email notification - success")
    void testSendNotification_success() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(emailSender).send(any(MimeMessage.class));
        emailNotificationService.send("test@mail.com", "Test Message");

        verify(emailSender, times(1)).createMimeMessage();
        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @DisplayName("Test send email notification and error something went wrong - Fail")
    void testSendNotification_fail() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailSendException("Error")).when(emailSender).send(any(MimeMessage.class));
        FUException exception = assertThrows(FUException.class, () -> emailNotificationService.send("test@mail.com", "Test Message"));
        assertEquals(ResultCode.UNABLE_TO_PROCESS, exception.getResultCode());

        verify(emailSender, times(1)).createMimeMessage();
        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }
}