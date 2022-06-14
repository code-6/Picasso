package org.novinomad.picasso.commons;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

public interface IRange {

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    default LocalDateTimeRange getDateRange() {
        return getStartDate() != null && getEndDate() != null
                ? new LocalDateTimeRange(getStartDate(), getEndDate())
                : null;
    }

    default boolean inPast() {
        return getEndDate().isBefore(LocalDateTime.now());
    }

    default boolean inFuture() {
        return getStartDate().isAfter(LocalDateTime.now());
    }

    default double getCompletenessPercent() {
        double completePercent = 0L;

        if(inPast()) return 100L;

        if(inFuture()) return 0L;

        Duration i = Duration.between(getStartDate(), getEndDate()); // 100%
        Duration j = Duration.between(getStartDate(), LocalDateTime.now()); // actual
        long iMinutes = i.getSeconds() / 60;
        long jMinutes = j.getSeconds() / 60;

        long onePercent = iMinutes / 100;

        long completenessPercentage = jMinutes / onePercent;

        return new BigDecimal(completenessPercentage)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
