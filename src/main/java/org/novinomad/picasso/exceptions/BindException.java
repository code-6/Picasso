package org.novinomad.picasso.exceptions;

import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.commons.utils.CommonMessageFormat;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.exceptions.base.PicassoException;

import java.util.Map;

public class BindException extends PicassoException {
    
    private static final String MSG_PATTERN = "Unable to bind: {} to the: {} for dates {} because: {}";
    
    public BindException(Employee employee,
                         Tour tour,
                         LocalDateTimeRange dateTimeRange,
                         String reason) {
        this(MSG_PATTERN, employee, tour, dateTimeRange, reason);
    }

    public BindException(Employee employee,
                         Tour tour,
                         LocalDateTimeRange dateTimeRange,
                         Map<Tour, LocalDateTimeRange> overlapsToursAndRanges) {
        super(buildOverlapsCauseMessage(employee, tour, dateTimeRange, overlapsToursAndRanges));
    }

    public BindException(String message, Object... args) {
        super(message, args);
    }

    public BindException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }

    public BindException(Throwable cause) {
        super(cause);
    }

    public BindException(String message,
                         Throwable cause,
                         boolean enableSuppression,
                         boolean writableStackTrace,
                         Object... args) {
        super(message, cause, enableSuppression, writableStackTrace, args);
    }

    private static String buildOverlapsCauseMessage(Employee employee,
                                                    Tour newBindTour,
                                                    LocalDateTimeRange newBindDateTimeRange,
                                                    Map<Tour, LocalDateTimeRange> overlappedToursAndRanges) {
        String baseMessage = CommonMessageFormat.format(MSG_PATTERN, employee, newBindTour, newBindDateTimeRange, "overlaps with: \n");

        StringBuilder overlapsCauseBuilder = new StringBuilder(baseMessage);

        for (Map.Entry<Tour, LocalDateTimeRange> overlapsTourAndRange : overlappedToursAndRanges.entrySet()) {

            Tour overlappedTour = overlapsTourAndRange.getKey();
            LocalDateTimeRange overlappedRange = overlapsTourAndRange.getValue();

            overlapsCauseBuilder.append(overlappedTour.toString()).append(" overlapped date range: ")
                    .append(overlappedRange.toString()).append("\n");
        }
        return overlapsCauseBuilder.toString();
    }
}
