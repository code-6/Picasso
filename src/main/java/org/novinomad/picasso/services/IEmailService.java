package org.novinomad.picasso.services;

public interface IEmailService {

    String DEFAULT_FROM = "noreply@picasso.org";
    void sendSimpleMail(String emailFrom, String emailTo, String title, String message);

    default void sendSimpleMail(String emailTo, String title, String message) {
        sendSimpleMail(DEFAULT_FROM, emailTo, title, message);
    }
}
