package org.novinomad.picasso.commons.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

@UtilityClass
public class PasswordUtil {

    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";


    public static String generateRandomPassword(int length) {
        return RandomStringUtils.random(length, characters);
    }

    public static String generateRandomPassword() {
        return generateRandomPassword(8);
    }
}
