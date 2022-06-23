package org.novinomad.picasso.dto.bind;

import lombok.Data;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.exceptions.TourBindException;

import java.util.*;

@Data
public class TourBindFormDTO {
    Tour tour;
    Map<Employee, Set<LocalDateTimeRange>> employeesToDateRanges = new HashMap<>();

    public void appointEmployee(Employee employee, LocalDateTimeRange localDateTimeRange) {
        Optional.ofNullable(employeesToDateRanges.get(employee))
                .ifPresentOrElse(dateRanges -> dateRanges.add(localDateTimeRange), () -> {
                    Set<LocalDateTimeRange> dateRanges = new TreeSet<>();
                    dateRanges.add(localDateTimeRange);
                    employeesToDateRanges.put(employee, dateRanges);
                });
    }

    public List<TourBind> forSave() throws TourBindException {

        List<TourBind> tourBinds = new ArrayList<>();

        for (Map.Entry<Employee, Set<LocalDateTimeRange>> entry : employeesToDateRanges.entrySet()) {

            Employee employee = entry.getKey();
            Set<LocalDateTimeRange> dateRanges = entry.getValue();

            for (LocalDateTimeRange dateRange : dateRanges) {
                tourBinds.add(new TourBind(employee, tour, dateRange.getStartDate(), dateRange.getEndDate()));
            }
        }
        return tourBinds;
    }
}
