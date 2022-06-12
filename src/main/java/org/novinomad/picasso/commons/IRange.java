package org.novinomad.picasso.commons;

import java.time.LocalDateTime;

public interface IRange {

    LocalDateTime getStartDate();

    LocalDateTime getEndDate();

    default LocalDateTimeRange getDateRange() {
        return getStartDate() != null && getEndDate() != null
                ? new LocalDateTimeRange(getStartDate(), getEndDate())
                : null;
    }
}
