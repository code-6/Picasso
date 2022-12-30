package org.novinomad.picasso.domain.dto.tour.filters;

import lombok.Data;
import org.novinomad.picasso.commons.ILocalDateTimeRange;

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
            endDate = startDate.plusYears(1);

        if(startDate.isAfter(endDate)) {
            // swap dates
            LocalDateTime date = startDate;
            startDate = endDate;
            endDate = date;
        }
    }
}
