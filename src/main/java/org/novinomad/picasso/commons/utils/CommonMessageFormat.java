package org.novinomad.picasso.commons.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonMessageFormat {

    /**
     * Placeholders format {}
     * */
    public static String format(String pattern, Object ... args) {
        pattern = pattern.replaceAll("(\\{})", "%s");
        return String.format(pattern, args);
    }
}
