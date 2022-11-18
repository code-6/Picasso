package org.novinomad.picasso.commons.utils;

import lombok.experimental.UtilityClass;

import java.text.MessageFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String format(String pattern, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            pattern = pattern.replaceAll("\\$\\{" + key + "}", val);
        }
        return pattern;
    }
}
