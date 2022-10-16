package org.novinomad.picasso.exceptions;

import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.commons.utils.CommonMessageFormat;
import org.novinomad.picasso.entities.domain.impl.TourParticipant;
import org.novinomad.picasso.entities.domain.impl.Tour;
import org.novinomad.picasso.exceptions.base.BaseException;

import java.util.Map;

public class BindException extends BaseException {
    
    private static final String MSG_PATTERN = "Unable to bind: {} to the: {} for dates {} because: {}";
    private static final String MSG_PATTERN_RU = "Не удалось связать: {} с: {} на даты {} причина: {}";

    public BindException(TourParticipant tourParticipant,
                         Tour tour,
                         IRange dateTimeRange,
                         String reason) {
        this(MSG_PATTERN, tourParticipant, tour, dateTimeRange, reason);
    }

    public BindException(TourParticipant tourParticipant,
                         Tour tour,
                         IRange dateTimeRange,
                         Map<Tour, IRange> overlapsToursAndRanges) {
        super(buildOverlapsCauseMessage(tourParticipant, tour, dateTimeRange, overlapsToursAndRanges));
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

    private static String buildOverlapsCauseMessage(TourParticipant tourParticipant,
                                                    Tour newBindTour,
                                                    IRange newBindDateTimeRange,
                                                    Map<Tour, IRange> overlappedToursAndRanges) {
        String baseMessage = CommonMessageFormat.format(MSG_PATTERN, tourParticipant, newBindTour, newBindDateTimeRange, "overlaps with: \n");

        StringBuilder overlapsCauseBuilder = new StringBuilder(baseMessage);

        for (Map.Entry<Tour, IRange> overlapsTourAndRange : overlappedToursAndRanges.entrySet()) {

            Tour overlappedTour = overlapsTourAndRange.getKey();
            IRange overlappedRange = overlapsTourAndRange.getValue();

            overlapsCauseBuilder.append(overlappedTour.toString()).append(" overlapped date range: ")
                    .append(overlappedRange.toString()).append("\n");
        }
        return overlapsCauseBuilder.toString();
    }
}
