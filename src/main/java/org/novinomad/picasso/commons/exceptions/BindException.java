package org.novinomad.picasso.commons.exceptions;

import lombok.Getter;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.commons.utils.CommonMessageFormat;
import org.novinomad.picasso.erm.entities.TourParticipant;
import org.novinomad.picasso.erm.entities.Tour;
import org.novinomad.picasso.commons.exceptions.base.CommonException;

import java.util.Map;

@Getter
public class BindException extends CommonException {
    
    private static final String MSG_PATTERN = "Unable to bind: {} to the: {} for dates {} because: {}";
    private static final String MSG_PATTERN_RU = "Не удалось связать: {} с: {} на даты {} причина: {}";

    private TourParticipant tourParticipant;
    private Tour tour;
    private IRange dateTimeRange;
    private Map<Tour, IRange> overlapsToursAndRanges;


    public BindException(TourParticipant tourParticipant,
                         Tour tour,
                         IRange dateTimeRange,
                         String reason) {
        this(MSG_PATTERN, tourParticipant, tour, dateTimeRange, reason);
        this.tourParticipant = tourParticipant;
        this.tour = tour;
        this.dateTimeRange = dateTimeRange;
    }

    public BindException(TourParticipant tourParticipant,
                         Tour tour,
                         IRange dateTimeRange,
                         Map<Tour, IRange> overlapsToursAndRanges) {
        super(buildOverlapsCauseMessage(tourParticipant, tour, dateTimeRange, overlapsToursAndRanges));
        this.tourParticipant = tourParticipant;
        this.tour = tour;
        this.dateTimeRange = dateTimeRange;
        this.overlapsToursAndRanges = overlapsToursAndRanges;
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
