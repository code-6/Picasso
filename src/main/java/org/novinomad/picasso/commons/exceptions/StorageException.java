package org.novinomad.picasso.commons.exceptions;

import org.novinomad.picasso.commons.exceptions.base.CommonException;

public class StorageException extends CommonException {

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
