package org.novinomad.picasso.dto.bind;

import lombok.Data;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.entities.impl.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class EmployeeBindModel {
    private Employee employee;
    private List<BindDateRange> bindIdsToDateRanges = new ArrayList<>();
}
