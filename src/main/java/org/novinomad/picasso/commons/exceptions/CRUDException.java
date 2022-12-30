package org.novinomad.picasso.commons.exceptions;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import org.novinomad.picasso.commons.enums.CRUD;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;


public class CRUDException extends CommonRuntimeException {


    public CRUDException(Throwable cause, @NotNull CRUD crud) {
        super(cause, "{} {}", crud.name(), cause.getMessage());
    }

    public CRUDException(Throwable cause, @NotNull CRUD crud, Object data) {
        super(cause, "{} {} {}", crud.name(), data, cause.getMessage());
    }
}
