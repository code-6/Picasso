package org.novinomad.picasso.exceptions;

import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.exceptions.base.PicassoException;

import java.time.LocalDateTime;

public class TourBindException extends PicassoException {
    
    private static final String MSG_PATTERN = "Unable to bind: {} to the: {} for dates {} - {} because: {}";
    
    public TourBindException(Employee employee, 
                             Tour tour, 
                             LocalDateTime startDate, 
                             LocalDateTime endDate, 
                             String reason) 
    {
        this(MSG_PATTERN, employee, tour, startDate, endDate, reason);
    }

    public TourBindException(String message, Object... args) {
        super(message, args);
    }

    public TourBindException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }

    public TourBindException(Throwable cause) {
        super(cause);
    }

    public TourBindException(String message, 
                             Throwable cause, 
                             boolean enableSuppression, 
                             boolean writableStackTrace, 
                             Object... args) 
    {
        super(message, cause, enableSuppression, writableStackTrace, args);
    }
}
