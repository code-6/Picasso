package org.novinomad.picasso.commons;

import lombok.Getter;
import lombok.Setter;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.novinomad.picasso.commons.utils.SpringContextUtil;
import org.novinomad.picasso.services.IUserService;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.ISO_8601;
import static org.novinomad.picasso.commons.utils.CommonDateUtils.COMMON;

@Getter
@Setter
public class LocalDateTimeRange implements Comparable<LocalDateTimeRange> {

    @DateTimeFormat(pattern = COMMON)
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = COMMON)
    private LocalDateTime endDate;

    public LocalDateTimeRange(LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate == null || endDate == null)
            throw new IllegalArgumentException("start and and dates may not be null");

        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(COMMON, SpringContextUtil.getBean(IUserService.class).getCurrentUserLocale());
        return startDate.format(dateTimeFormatter) + " ~ " + endDate.format(dateTimeFormatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalDateTimeRange that = (LocalDateTimeRange) o;
        return Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

    @Override
    public int compareTo(LocalDateTimeRange o) {

        if(startDate.equals(o.getStartDate()))
            return endDate.compareTo(o.getEndDate());

        return startDate.compareTo(o.getStartDate());
    }

    public static LocalDateTimeRange parse(String text) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(CommonDateUtils.COMMON, SpringContextUtil.getBean(IUserService.class).getCurrentUserLocale());

        String[] split = text.split("\s[~\\-]\s");

        if(split.length != 2) {
            String errorMessage = String.format("something went wrong while split following string: %s expected array.length = 2, but was: %d",
                    text, split.length);
            throw new RuntimeException(errorMessage);
        }
        return new LocalDateTimeRange(LocalDateTime.parse(split[0], dateTimeFormatter), LocalDateTime.parse(split[1], dateTimeFormatter));
    }

    public static LocalDateTimeRange parse(String text, Locale locale) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(CommonDateUtils.COMMON, locale);

        String[] split = text.split("\s[~\\-]\s");

        if(split.length != 2) {
            String errorMessage = String.format("something went wrong while split following string: %s expected array.length = 2, but was: %d",
                    text, split.length);
            throw new RuntimeException(errorMessage);
        }
        return new LocalDateTimeRange(LocalDateTime.parse(split[0], dateTimeFormatter), LocalDateTime.parse(split[1], dateTimeFormatter));
    }
}
