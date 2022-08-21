package org.novinomad.picasso.dto.bind;

import lombok.Data;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.exceptions.BindException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class TourBindModel {

    private Tour tour = new Tour();

    private List<EmployeeBindModel> employees = new ArrayList<>();

    public void bindEmployee(Employee employee) {
        if (tour != null) {
            bindEmployee(employee, tour.getDateRange());
        }
    }

    public void bindEmployee(Employee employee, LocalDateTimeRange dateRange) {
        getBoundEmployee(employee).ifPresentOrElse(e -> {
            List<LocalDateTimeRange> dateRanges = e.getDateRanges();
            if (!dateRanges.contains(dateRange))
                dateRanges.add(dateRange);
        }, () -> {
            EmployeeBindModel employeeBindModel = new EmployeeBindModel();
            employeeBindModel.getDateRanges().add(dateRange);
            employees.add(employeeBindModel);
        });
    }

    public Optional<EmployeeBindModel> getBoundEmployee(Employee employee) {
        return employees.stream().filter(e -> e.getEmployee().equals(employee)).findAny();
    }

    public List<TourBind> toEntities() throws BindException {
        List<TourBind> tourBinds = new ArrayList<>();

        for (EmployeeBindModel employeeBindModel : employees) {
            for (LocalDateTimeRange dateRange : employeeBindModel.getDateRanges()) {
                tourBinds.add(new TourBind(employeeBindModel.getEmployee(), tour, dateRange));
            }
        }
        return tourBinds;
    }

    public boolean canSubmit() {
        return tour != null
                && tour.getId() != null
                && !employees.isEmpty()
                && employees.get(0).getEmployee() != null
                && employees.get(0).getEmployee().getId() != null;
    }
}
