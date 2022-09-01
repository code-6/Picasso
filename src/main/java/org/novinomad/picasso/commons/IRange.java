package org.novinomad.picasso.commons;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.COMMON;

public interface IRange {

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    default String getEndDateAsString() {
        return getEndDateAsString(null, Locale.ENGLISH);
    }

    default String getEndDateAsString(String format, Locale locale) {
        if(getEndDate() == null) return "";

        if(StringUtils.isBlank(format)) format = COMMON;

        return getEndDate().format(DateTimeFormatter.ofPattern(format, locale));
    }

    default String getStartDateAsString() {
        return getStartDateAsString(null, Locale.ENGLISH);
    }
    default String getStartDateAsString(String format, Locale locale) {
        if(getStartDate() == null) return "";

        if(StringUtils.isBlank(format)) format = COMMON;

        return getStartDate().format(DateTimeFormatter.ofPattern(format, locale));
    }

    default LocalDateTimeRange getDateRange() {
        return getStartDate() != null && getEndDate() != null
                ? new LocalDateTimeRange(getStartDate(), getEndDate())
                : null;
    }

    default boolean inPast() {
        return getEndDate() != null && getEndDate().isBefore(LocalDateTime.now());
    }

    default boolean inFuture() {
        return getStartDate() != null && getStartDate().isAfter(LocalDateTime.now());
    }

    default double getCompletenessPercent() {

        if(inPast()) return 100L;

        if(inFuture()) return 0L;

        if(getStartDate() == null || getEndDate() == null) return 0d;

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
