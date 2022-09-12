package org.novinomad.picasso.dto.filters;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.entities.domain.impl.Employee;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourBindFilter extends TourFilter {

    private List<Employee.Type> employeeTypes = new ArrayList<>();

    private List<Long> employeeIds = new ArrayList<>();

    public TourBindFilter() {
        super();
    }

    public TourBindFilter(LocalDateTime startDate, LocalDateTime endDate) {
        super(startDate, endDate);
    }
}
