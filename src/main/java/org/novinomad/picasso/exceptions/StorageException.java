package org.novinomad.picasso.exceptions;

import org.novinomad.picasso.exceptions.base.PicassoException;

public class StorageException extends PicassoException {

    public StorageException() {
    }

    public StorageException(String message, Object... args) {
        super(message, args);
    }

    public StorageException(Throwable cause, String message, Object... args) {
        super(cause, message, args);
    }

    public StorageException(Throwable cause) {
        super(cause);
    }

    public StorageException(Throwable cause, String message, boolean enableSuppression, boolean writableStackTrace, Object... args) {
        super(cause, message, enableSuppression, writableStackTrace, args);
    }
}
