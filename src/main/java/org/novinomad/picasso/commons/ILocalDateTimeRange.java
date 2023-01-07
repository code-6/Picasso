package org.novinomad.picasso.commons;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.novinomad.picasso.commons.utils.CommonDurationUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.UI_DATE_TIME_NO_SEC;


public interface ILocalDateTimeRange {

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    default String getDurationAsString() {
        return CommonDurationUtils.formatDurationWordsNoSec(getDuration().toMillis(), true, true);
    }

    default boolean between(LocalDateTime startDate, LocalDateTime endDate) {
        return (getStartDate().isAfter(startDate) || getStartDate().isEqual(startDate))
                && (getEndDate().isBefore(endDate) || getEndDate().isEqual(endDate));
    }

    default boolean between(LocalDateTimeRange range) {
        return (getStartDate().isAfter(range.getStartDate()) || getStartDate().isEqual(range.getStartDate()))
                && (getEndDate().isBefore(range.getEndDate()) || getEndDate().isEqual(range.getEndDate()));
    }

    default Duration getDuration() {
        return Duration.between(getStartDate(), getEndDate());
    }

    default String getEndDateAsString() {
        return getEndDateAsString(UI_DATE_TIME_NO_SEC, CommonDateUtils.DEFAULT_LOCALE);
    }

    default String getEndDateAsString(String format, Locale locale) {
        if(getEndDate() == null) return "";

        if(StringUtils.isBlank(format)) format = UI_DATE_TIME_NO_SEC;

        return getEndDate().format(DateTimeFormatter.ofPattern(format, locale));
    }

    default String getStartDateAsString() {
        return getStartDateAsString(null, CommonDateUtils.DEFAULT_LOCALE);
    }
    default String getStartDateAsString(String format, Locale locale) {
        if (getStartDate() == null) return "";

        if (StringUtils.isBlank(format)) format = UI_DATE_TIME_NO_SEC;

        return getStartDate().format(DateTimeFormatter.ofPattern(format, locale));
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
