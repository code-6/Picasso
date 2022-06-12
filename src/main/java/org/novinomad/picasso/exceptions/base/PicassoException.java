package org.novinomad.picasso.exceptions.base;

import org.novinomad.picasso.commons.utils.CommonMessageFormat;

public class PicassoException extends Exception {

    public PicassoException() {
    }

    public PicassoException(String message, Object ... args) {
        super(CommonMessageFormat.format(message, args));
    }

    public PicassoException(Throwable cause, String message, Object ... args) {
        super(CommonMessageFormat.format(message, args), cause);
    }

    public PicassoException(Throwable cause) {
        super(cause);
    }

    public PicassoException(Throwable cause,
                            String message,
                            boolean enableSuppression,
                            boolean writableStackTrace,
                            Object ... args)
    {
        super(CommonMessageFormat.format(message, args), cause, enableSuppression, writableStackTrace);
    }
}
