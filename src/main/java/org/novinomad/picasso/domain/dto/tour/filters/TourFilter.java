package org.novinomad.picasso.domain.dto.tour.filters;

import lombok.Data;
import org.novinomad.picasso.commons.ILocalDateTimeRange;
import org.novinomad.picasso.commons.LocalDateTimeRange;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Data
public class TourFilter implements ILocalDateTimeRange {
    
    protected LocalDateTime startDate;

    protected LocalDateTime endDate;

    protected List<Long> tourIds = new ArrayList<>();

    public TourFilter() {
        normalize();
    }

    public TourFilter(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;

        normalize();
    }

    public void normalize() {
        LocalDateTime currentDate = LocalDateTime.now();

        if(startDate == null)
            startDate = currentDate.with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);

        if(endDate == null)
            endDate = currentDate.with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MIN);

        if(startDate.isAfter(endDate)) {
            // swap dates
            LocalDateTime date = startDate;
            startDate = endDate;
            endDate = date;
        }
    }

    public LocalDateTimeRange getLocalDateTimeRange() {
        return new LocalDateTimeRange(startDate, endDate);
    }
}
