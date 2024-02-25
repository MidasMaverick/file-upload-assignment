package com.fileupload.service.impl;

import com.fileupload.exception.FUException;
import com.fileupload.model.ResultCode;
import com.fileupload.service.NotificationService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("emailNotificationService")
public class EmailNotificationServiceImpl implements NotificationService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailNotificationServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }
    @Override
    public void send(String sendTo, String message) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        try {
            helper.setTo(sendTo);
            helper.setSubject("File Upload Notification");
            helper.setText(message);
            emailSender.send(mimeMessage);
        } catch (Exception ex) {
            log.error("Failed to send email notification.", ex);
            throw new FUException(ResultCode.UNABLE_TO_PROCESS);
        }
    }
}
