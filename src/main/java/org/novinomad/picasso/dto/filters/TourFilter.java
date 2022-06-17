package org.novinomad.picasso.dto.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.tomcat.jni.Local;
import org.novinomad.picasso.commons.LocalDateTimeRange;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Data
@AllArgsConstructor
public class TourFilter {
    LocalDateTime startDate;
    LocalDateTime endDate;
    String tourName;

    public TourFilter(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TourFilter() {
        LocalDateTime currentDate = LocalDateTime.now();

        startDate = currentDate.with(TemporalAdjusters.firstDayOfYear());
        endDate = currentDate.with(TemporalAdjusters.lastDayOfYear());
    }
}
