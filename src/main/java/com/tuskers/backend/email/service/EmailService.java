package com.tuskers.backend.email.service;

import com.tuskers.backend.email.dto.EmailDto;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface EmailService {
   void sendMail(EmailDto emailDto) throws MessagingException, UnsupportedEncodingException;
}
