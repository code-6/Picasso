package org.novinomad.picasso.exceptions.base;

import org.novinomad.picasso.commons.utils.CommonMessageFormat;
import org.novinomad.picasso.commons.utils.SpringContextUtil;
import org.novinomad.picasso.services.IUserService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BaseException extends Exception {

    protected final Map<Locale, String> LOCALIZED_MESSAGES = new HashMap<>();

    protected final Locale USER_LOCALE;

    public BaseException() {
        USER_LOCALE = SpringContextUtil.getBean(IUserService.class).getCurrentUserLocale();
    }

    public BaseException(String message, Object ... args) {
        super(CommonMessageFormat.format(message, args));
        USER_LOCALE = SpringContextUtil.getBean(IUserService.class).getCurrentUserLocale();
    }

    public BaseException(Throwable cause, String message, Object ... args) {
        super(CommonMessageFormat.format(message, args), cause);
        USER_LOCALE = SpringContextUtil.getBean(IUserService.class).getCurrentUserLocale();
    }

    public BaseException(Throwable cause) {
        super(cause);
        USER_LOCALE = SpringContextUtil.getBean(IUserService.class).getCurrentUserLocale();
    }

    public BaseException(Throwable cause,
                         String message,
                         boolean enableSuppression,
                         boolean writableStackTrace,
                         Object ... args)
    {
        super(CommonMessageFormat.format(message, args), cause, enableSuppression, writableStackTrace);
        USER_LOCALE = SpringContextUtil.getBean(IUserService.class).getCurrentUserLocale();
    }

    public BaseException withLocalizedMessage(Locale locale, String exceptionMessage) {
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
