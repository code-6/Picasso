package org.novinomad.picasso.commons;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.ISO_8601;

@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocalDateTimeRange implements Comparable<LocalDateTimeRange> {

    @DateTimeFormat(pattern = ISO_8601)
    LocalDateTime startDate;

    @DateTimeFormat(pattern = ISO_8601)
    LocalDateTime endDate;

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ISO_8601);
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
