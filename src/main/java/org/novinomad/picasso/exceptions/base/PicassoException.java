package org.novinomad.picasso.exceptions.base;

import org.novinomad.picasso.commons.utils.CommonMessageFormat;

public class PicassoException extends Exception {

    public PicassoException() {
    }

    public PicassoException(String message, Object ... args) {
        super(CommonMessageFormat.format(message, args));
    }

    public PicassoException(String message, Throwable cause, Object ... args) {
        super(CommonMessageFormat.format(message, args), cause);
    }

    public PicassoException(Throwable cause) {
        super(cause);
    }

    public PicassoException(String message,
                            Throwable cause,
                            boolean enableSuppression,
                            boolean writableStackTrace,
                            Object ... args)
    {
        super(CommonMessageFormat.format(message, args), cause, enableSuppression, writableStackTrace);
    }
}
