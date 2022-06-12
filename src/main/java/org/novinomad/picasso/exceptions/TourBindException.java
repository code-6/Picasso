package org.novinomad.picasso.exceptions;

import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.commons.utils.CommonMessageFormat;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.exceptions.base.PicassoException;

import java.util.Map;

public class TourBindException extends PicassoException {
    
    private static final String MSG_PATTERN = "Unable to bind: {} to the: {} for dates {} because: {}";
    
    public TourBindException(Employee employee, 
                             Tour tour, 
                             LocalDateTimeRange dateTimeRange,
                             String reason) 
    {
        this(MSG_PATTERN, employee, tour, dateTimeRange, reason);
    }

    public TourBindException(Employee employee,
                             Tour tour,
                             LocalDateTimeRange dateTimeRange,
                             Map<Tour, LocalDateTimeRange> overlapsToursAndRanges)
    {
        super(buildOverlapsCauseMessage(employee, tour, dateTimeRange, overlapsToursAndRanges));
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

    private static String buildOverlapsCauseMessage(Employee employee,
                                                    Tour tour,
                                                    LocalDateTimeRange dateTimeRange,
                                                    Map<Tour, LocalDateTimeRange> overlapsToursAndRanges)
    {
        String baseMessage = CommonMessageFormat.format(MSG_PATTERN, employee, tour, dateTimeRange, "overlaps with: \n");

        StringBuilder overlapsCauseBuilder = new StringBuilder(baseMessage);

        for (Map.Entry<Tour, LocalDateTimeRange> overlapsTourAndRange : overlapsToursAndRanges.entrySet()) {

            Tour overlapsTour = overlapsTourAndRange.getKey();
            LocalDateTimeRange overlapsRange = overlapsTourAndRange.getValue();

            overlapsCauseBuilder.append(overlapsTour.toString()).append(" ")
                    .append(overlapsRange.toString()).append("\n");
        }
        return overlapsCauseBuilder.toString();
    }
}
