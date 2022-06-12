package org.novinomad.picasso.commons;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.ISO_8601;

@RequiredArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocalDateTimeRange {

    @DateTimeFormat(pattern = ISO_8601)
    LocalDateTime startDate;

    @DateTimeFormat(pattern = ISO_8601)
    LocalDateTime endDate;

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ISO_8601);
        return startDate.format(dateTimeFormatter) + " ~ " + endDate.format(dateTimeFormatter);
    }
}
