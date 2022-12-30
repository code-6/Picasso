package org.novinomad.picasso.services.common;

public interface EmailService {

    String DEFAULT_FROM = "noreply@picasso.org";
    void sendSimpleMail(String emailFrom, String emailTo, String title, String message);

    default void sendSimpleMail(String emailTo, String title, String message) {
        sendSimpleMail(DEFAULT_FROM, emailTo, title, message);
    }
}
