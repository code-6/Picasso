package org.novinomad.picasso.dto.bind;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.exceptions.BindException;

import java.util.*;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourBindFormDTO {
    Tour tour;
    Map<Employee, Set<LocalDateTimeRange>> employeesToDateRanges = new HashMap<>();

    Boolean hideTourBindForm = true;

    public void appointEmployee(Employee employee, LocalDateTimeRange localDateTimeRange) {
        Optional.ofNullable(employeesToDateRanges.get(employee))
                .ifPresentOrElse(dateRanges -> dateRanges.add(localDateTimeRange), () -> {
                    Set<LocalDateTimeRange> dateRanges = new TreeSet<>();

                    if(localDateTimeRange != null)
                        dateRanges.add(localDateTimeRange);

                    employeesToDateRanges.put(employee, dateRanges);
                });
    }

    public void appointEmployee(Employee employee) {
        appointEmployee(employee, null);
    }

    public List<TourBind> forSave() throws BindException {

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
