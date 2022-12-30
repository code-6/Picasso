package org.novinomad.picasso.commons.exceptions.base;

import org.novinomad.picasso.commons.utils.CommonMessageFormat;
import org.novinomad.picasso.commons.utils.SpringContextUtil;
import org.novinomad.picasso.services.auth.UserService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CommonException extends Exception {

    protected final Map<Locale, String> LOCALIZED_MESSAGES = new HashMap<>();

    protected final Locale USER_LOCALE;

    private static final UserService USER_SERVICE;

    static {
        USER_SERVICE = SpringContextUtil.getBean(UserService.class);
    }

    public CommonException() {
        USER_LOCALE = USER_SERVICE.getCurrentUserLocale();
    }

    public CommonException(String message, Object ... args) {
        super(CommonMessageFormat.format(message, args));
        USER_LOCALE = USER_SERVICE.getCurrentUserLocale();
    }

    public CommonException(Throwable cause, String message, Object ... args) {
        super(CommonMessageFormat.format(message, args), cause);
        USER_LOCALE = USER_SERVICE.getCurrentUserLocale();
    }

    public CommonException(Throwable cause) {
        super(cause);
        USER_LOCALE = USER_SERVICE.getCurrentUserLocale();
    }

    public CommonException(Throwable cause,
                           String message,
                           boolean enableSuppression,
                           boolean writableStackTrace,
                           Object ... args)
    {
        super(CommonMessageFormat.format(message, args), cause, enableSuppression, writableStackTrace);
        USER_LOCALE = USER_SERVICE.getCurrentUserLocale();
    }

    public CommonException withLocalizedMessage(Locale locale, String exceptionMessage) {
        LOCALIZED_MESSAGES.put(locale, exceptionMessage);
        return this;
    }

    @Override
    public String getLocalizedMessage() {
        return LOCALIZED_MESSAGES.get(USER_LOCALE);
    }

    public String getLocalizedMessage(Locale locale) {
        return LOCALIZED_MESSAGES.get(locale);
    }
}
