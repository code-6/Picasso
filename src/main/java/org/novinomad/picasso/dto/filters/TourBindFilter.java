package org.novinomad.picasso.dto.filters;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourBindFilter {
    @DateTimeFormat(pattern = CommonDateUtils.COMMON)
    LocalDateTime startDate;
    @DateTimeFormat(pattern = CommonDateUtils.COMMON)
    LocalDateTime endDate;

    List<Employee.Type> employeeTypes = new ArrayList<>();

    List<Long> employeeIds = new ArrayList<>();
    List<Long> tourIds = new ArrayList<>();

    boolean hideFilterForm = true;

    public TourBindFilter() {
        normalize();
    }

    public void normalize() {
        LocalDateTime currentDate = LocalDateTime.now().with(LocalTime.MIN);

        if(startDate == null)
            startDate = currentDate;

        if(endDate == null)
            endDate = startDate.plusYears(1).with(LocalTime.MIN);

        if(startDate.isAfter(endDate)) {
            // swap dates
            LocalDateTime date = startDate;
            startDate = endDate;
            endDate = date;
        }
    }
}
