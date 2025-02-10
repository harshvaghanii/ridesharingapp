package com.vaghani.project.ridesharing.ridesharingapp;

import com.vaghani.project.ridesharing.ridesharingapp.services.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RidesharingappApplicationTests {

    @Autowired
    private EmailSenderService emailSenderService;


    @Test
    void contextLoads() {

        String[] recipients = {"harshvaghani1911@gmail.com",
                "bohab15177@nike4s.com",
                "vaghanivlogs@gmail.com"
        };
        String subject = "What a Superbowl Sunday!!";
        String body = "Hello Everyone! Hope you have a great next week!!\n" +
                "Goodnight!!\n" +
                "This is a new message, again!!";
        emailSenderService.sendEmail(recipients,
                subject,
                body);
    }

}
