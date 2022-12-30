package org.novinomad.picasso.commons.exceptions;

import lombok.Getter;
import org.novinomad.picasso.commons.ILocalDateTimeRange;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.commons.utils.CommonMessageFormat;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.commons.exceptions.base.CommonException;

import java.util.Map;

@Getter
public class BindException extends CommonException {
    
    private static final String MSG_PATTERN = "Unable to bind: {} to the: {} for dates {} because: {}";

    private TourParticipant tourParticipant;
    private Tour tour;
    private LocalDateTimeRange dateTimeRange;
    private Map<Tour, LocalDateTimeRange> overlapsToursAndRanges;


    public BindException(TourParticipant tourParticipant,
                         Tour tour,
                         LocalDateTimeRange dateTimeRange,
                         String reason) {
        this(MSG_PATTERN, tourParticipant, tour, dateTimeRange, reason);
        this.tourParticipant = tourParticipant;
        this.tour = tour;
        this.dateTimeRange = dateTimeRange;
    }

    public BindException(TourParticipant tourParticipant,
                         Tour tour,
                         LocalDateTimeRange dateTimeRange,
                         Map<Tour, LocalDateTimeRange> overlapsToursAndRanges) {
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
                                                    LocalDateTimeRange newBindDateTimeRange,
                                                    Map<Tour, LocalDateTimeRange> overlappedToursAndRanges) {
        String baseMessage = CommonMessageFormat.format(MSG_PATTERN, tourParticipant, newBindTour, newBindDateTimeRange, "overlaps with: \n");

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
