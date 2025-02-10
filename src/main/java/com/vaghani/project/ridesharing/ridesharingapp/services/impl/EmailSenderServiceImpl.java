package com.vaghani.project.ridesharing.ridesharingapp.services.impl;

import com.vaghani.project.ridesharing.ridesharingapp.services.EmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        sendEmail(new String[]{toEmail}, subject, body);
    }

    @Override
    public void sendEmail(String[] recipients, String subject, String body) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setBcc(recipients);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);
            log.info("Attempting to send the email to {}", Arrays.toString(recipients));
            javaMailSender.send(simpleMailMessage);
            log.info("Successfully sent the email to {}", Arrays.toString(recipients));
        } catch (Exception exception) {
            log.info("Cannot send email! Exception: {}", exception.getMessage());
        }
    }

}
