package org.novinomad.picasso.dto.bind;

import lombok.Data;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.entities.impl.Employee;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmployeeBindModel {
    private Employee employee;
    private List<LocalDateTimeRange> dateRanges = new ArrayList<>();
}
