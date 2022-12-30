package org.novinomad.picasso.commons.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class RegexUtil {

    public static final Pattern EMAIL;

    static {
        EMAIL = Pattern.compile("^\\S+@\\S+\\.\\S+$");
    }

}

