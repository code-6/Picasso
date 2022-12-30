package org.novinomad.picasso.commons.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class ValidationUtil {

    public static boolean isValidEmail(String email) {
        return StringUtils.isNotBlank(email) && RegexUtil.EMAIL.matcher(email).matches();
    }

}
