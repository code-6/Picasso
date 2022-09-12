package org.novinomad.picasso.dto.bind;

import lombok.Data;
import org.novinomad.picasso.entities.domain.impl.Employee;

import java.util.ArrayList;
import java.util.List;

@Data
public class EmployeeBindModel {
    private Employee employee;
    private List<BindDateRange> bindIdsToDateRanges = new ArrayList<>();
}
