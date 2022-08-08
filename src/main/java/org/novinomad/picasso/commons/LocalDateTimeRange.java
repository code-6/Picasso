package org.novinomad.picasso.commons;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.ISO_8601;
import static org.novinomad.picasso.commons.utils.CommonDateUtils.RU_WITHOUT_SECONDS;

@Getter
public class LocalDateTimeRange implements Comparable<LocalDateTimeRange> {

    @DateTimeFormat(pattern = ISO_8601)
    private final LocalDateTime startDate;

    @DateTimeFormat(pattern = ISO_8601)
    private final LocalDateTime endDate;

    public LocalDateTimeRange(LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate == null || endDate == null)
            throw new IllegalArgumentException("start and and dates may not be null");

        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(RU_WITHOUT_SECONDS);
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
}
