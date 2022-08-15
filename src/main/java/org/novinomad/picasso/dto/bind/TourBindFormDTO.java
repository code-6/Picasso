package org.novinomad.picasso.dto.bind;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    boolean dateRangeChosenAtLeasOnce = false;
    Tour tour;
    List<EmployeeBind> employeeBinds = new ArrayList<>();

    Boolean hideTourBindForm = true;

    private Optional<EmployeeBind> getEmployeeBindOf(Employee employee) {
        return employeeBinds.stream().filter(eb -> employee.equals(eb.getEmployee())).findAny();
    }

    public void appointEmployee(Employee employee, LocalDateTimeRange localDateTimeRange) {
        getEmployeeBindOf(employee)
                .ifPresentOrElse(employeeBind -> employeeBind.addDateRange(localDateTimeRange), () -> {
                    EmployeeBind employeeBind = new EmployeeBind(employee);
                    employeeBind.addDateRange(localDateTimeRange);
                    employeeBinds.add(employeeBind);
                });
    }

    public void appointEmployee(Employee employee) {
        appointEmployee(employee, null);
    }

    public void appointEmployee(List<Employee> employees) {
        employees.forEach(this::appointEmployee);
    }

    public List<TourBind> forSave() throws BindException {

        List<TourBind> tourBinds = new ArrayList<>();

        for (EmployeeBind employeeBind : employeeBinds) {

            Employee employee = employeeBind.getEmployee();
            List<LocalDateTimeRange> dateRanges = employeeBind.getLocalDateTimeRanges();

            for (LocalDateTimeRange dateRange : dateRanges) {
                tourBinds.add(new TourBind(employee, tour, dateRange.getStartDate(), dateRange.getEndDate()));
            }
        }
        return tourBinds;
    }

    @Data
    @NoArgsConstructor
    public static class EmployeeBind {
        private Employee.Type employeeType;
        private Employee employee;
        private List<LocalDateTimeRange> localDateTimeRanges = new ArrayList<>();

        public EmployeeBind(Employee employee) {
            this.employee = employee;
        }

        public void addDateRange(LocalDateTimeRange localDateTimeRange) {
            if (localDateTimeRange != null)
                localDateTimeRanges.add(localDateTimeRange);
        }
    }

    public boolean dateRangeChosenAtLeasOnce() {
        return dateRangeChosenAtLeasOnce;
    }
}
