package org.novinomad.picasso.dto.filters;

import lombok.Data;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Data
public class TourFilter implements IRange {
    @DateTimeFormat(pattern = CommonDateUtils.COMMON)
    LocalDateTime startDate;
    @DateTimeFormat(pattern = CommonDateUtils.COMMON)
    LocalDateTime endDate;

    List<Long> tourIds = new ArrayList<>();

    public TourFilter() {
        normalize();
    }

    public TourFilter(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;

        normalize();
    }

    public String formatStartDate(String pattern) {
        //01.01.2022 00:00
        return startDate.format(DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH));
    }

    public String formatEndDate(String pattern) {
        return endDate.format(DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH));
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
