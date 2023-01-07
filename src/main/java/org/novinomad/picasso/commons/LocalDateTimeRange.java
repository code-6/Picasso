package org.novinomad.picasso.commons;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Embeddable
public class LocalDateTimeRange implements ILocalDateTimeRange, Comparable<ILocalDateTimeRange> {

    @Getter
    @Setter
    @DateTimeFormat(pattern = CommonDateUtils.UI_DATE_TIME_NO_SEC)
    private LocalDateTime startDate;

    @Getter
    @Setter
    @DateTimeFormat(pattern = CommonDateUtils.UI_DATE_TIME_NO_SEC)
    private LocalDateTime endDate;

    public LocalDateTimeRange() {
        startDate = LocalDateTime.now();
        endDate = LocalDateTime.now();;
    }

    public LocalDateTimeRange(@NotNull(message = "start date may not be null") LocalDateTime startDate,
                              @NotNull(message = "end date may not be null") LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return getStartDateAsString() + " ~ " + getEndDateAsString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocalDateTimeRange that = (LocalDateTimeRange) o;
        return startDate.equals(that.startDate) && endDate.equals(that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

    @Override
    public int compareTo(ILocalDateTimeRange dateTimeRange) {

        if(startDate.equals(dateTimeRange.getStartDate()))
            return endDate.compareTo(dateTimeRange.getEndDate());

        return startDate.compareTo(dateTimeRange.getStartDate());
    }

    public static LocalDateTimeRange parse(@NotBlank(message = "local date time range string may not be null") String text) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(CommonDateUtils.UI_DATE_TIME_NO_SEC, CommonDateUtils.DEFAULT_LOCALE);

        String[] split = text.split("\s[~\\-]\s");

        if(split.length != 2) {
            String errorMessage = String.format("something went wrong while split following string: %s expected array.length = 2, but was: %d",
                    text, split.length);
            throw new RuntimeException(errorMessage);
        }
        return new LocalDateTimeRange(LocalDateTime.parse(split[0], dateTimeFormatter), LocalDateTime.parse(split[1], dateTimeFormatter));
    }

    public static LocalDateTimeRange parse(@NotBlank(message = "local date time range string may not be null") String text,
                                           @NotNull(message = " locale may not be null") Locale locale) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(CommonDateUtils.UI_DATE_TIME_NO_SEC, locale);

        String[] split = text.split("\s[~\\-]\s");

        if(split.length != 2) {
            String errorMessage = String.format("something went wrong while split following string: %s expected array.length = 2, but was: %d",
                    text, split.length);
            throw new RuntimeException(errorMessage);
        }
        return new LocalDateTimeRange(LocalDateTime.parse(split[0], dateTimeFormatter), LocalDateTime.parse(split[1], dateTimeFormatter));
    }
}
