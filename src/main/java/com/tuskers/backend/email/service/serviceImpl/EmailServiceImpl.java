package com.tuskers.backend.email.service.serviceImpl;

import com.tuskers.backend.email.dto.EmailDto;
import com.tuskers.backend.email.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.from-email}")
    private String fromEmail;

    @Value("${spring.mail.from-name}")
    private String fromName;

    @Override
    public void sendMail(EmailDto emailDto) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromEmail, fromName);
        helper.setTo(emailDto.getToEmail());
        helper.setSubject(emailDto.getSubject());
        helper.setText(emailDto.getContent(), false);

        javaMailSender.send(message);
        logger.info("Email send successfully to :{}", emailDto.getToEmail());
    }
}
