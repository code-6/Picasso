package org.novinomad.picasso.commons.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

@UtilityClass
public class PasswordUtil {

    public static final int DEFAULT_MIN_LENGTH = 8;

    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";


    public static String generateRandomPassword(int length) {
        return RandomStringUtils.random(length, characters);
    }

    public static String generateRandomPassword() {
        return generateRandomPassword(DEFAULT_MIN_LENGTH);
    }
}
