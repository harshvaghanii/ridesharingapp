package com.vaghani.project.ridesharing.ridesharingapp.services;

public interface EmailSenderService {

    void sendEmail(String toEmail, String subject, String body);

    void sendEmail(String[] recipients, String subject, String body);

}
