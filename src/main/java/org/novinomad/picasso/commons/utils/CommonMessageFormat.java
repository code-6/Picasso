package org.novinomad.picasso.commons.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonMessageFormat {

    /**
     * @param pattern Message with placeholders, as placeholder use {}. Example: "This is my message with placeholder {}"
     * @param args, values to be placed instead of placeholders.
     *
     * @return formatted string, with placeholders to values replacement.
     * */
    public static String format(String pattern, Object ... args) {
        pattern = pattern.replaceAll("(\\{})", "%s");
        return String.format(pattern, args);
    }
}
