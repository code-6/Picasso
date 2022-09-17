package org.novinomad.picasso.exceptions.base;

import org.novinomad.picasso.commons.utils.CommonMessageFormat;

public class BaseException extends Exception {

    public BaseException() {
    }

    public BaseException(String message, Object ... args) {
        super(CommonMessageFormat.format(message, args));
    }

    public BaseException(Throwable cause, String message, Object ... args) {
        super(CommonMessageFormat.format(message, args), cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(Throwable cause,
                         String message,
                         boolean enableSuppression,
                         boolean writableStackTrace,
                         Object ... args)
    {
        super(CommonMessageFormat.format(message, args), cause, enableSuppression, writableStackTrace);
    }
}
