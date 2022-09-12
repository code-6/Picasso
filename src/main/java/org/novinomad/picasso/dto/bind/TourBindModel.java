package org.novinomad.picasso.dto.bind;

import lombok.Data;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.entities.domain.impl.Employee;
import org.novinomad.picasso.entities.domain.impl.Tour;
import org.novinomad.picasso.entities.domain.impl.TourBind;
import org.novinomad.picasso.exceptions.BindException;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class TourBindModel{

    private Tour tour = new Tour();

    private List<EmployeeBindModel> employees = new ArrayList<>();

    public void bindEmployee(Employee employee) {
        if (tour != null) {
            bindEmployee(null, employee, tour.getDateRange());
        }
    }

    public void bindEmployee(Long bindId, Employee employee, IRange dateRange) {
        getBoundEmployee(employee).ifPresentOrElse(employeeBindModel -> {
            List<BindDateRange> dateRanges = employeeBindModel.getBindIdsToDateRanges();
            if (dateRanges.stream().noneMatch(bindDateRange -> bindDateRange.getDateRange().equals(dateRange)))
                dateRanges.add(new BindDateRange(bindId, dateRange));
        }, () -> {
            EmployeeBindModel employeeBindModel = new EmployeeBindModel();
            employeeBindModel.getBindIdsToDateRanges().add(new BindDateRange(bindId, dateRange));
            employeeBindModel.setEmployee(employee);
            employees.add(employeeBindModel);
        });
    }

    public Optional<EmployeeBindModel> getBoundEmployee(Employee employee) {
        return employees.isEmpty()
                ? Optional.empty()
                : employees.stream()
                .filter(e -> e != null && e.getEmployee() != null && e.getEmployee().equals(employee))
                .findAny();
    }

    public List<TourBind> toEntities() throws BindException {
        List<TourBind> tourBinds = new ArrayList<>();

        if (employees.isEmpty()) tourBinds.add(new TourBind(null, tour, tour.getDateRange()));
        else
            for (EmployeeBindModel employeeBindModel : employees)
                for (BindDateRange bindDateRange : employeeBindModel.getBindIdsToDateRanges())
                    tourBinds.add(new TourBind(bindDateRange.getBindId(), employeeBindModel.getEmployee(), tour, bindDateRange.getDateRange()));

        return tourBinds;
    }

    public static TourBindModel fromEntities(List<TourBind> tourBinds) {
        Map<Tour, List<TourBind>> collect = tourBinds.stream().collect(Collectors.groupingBy(TourBind::getTour));
        int toursCount = collect.keySet().size();
        if (toursCount > 1)
            throw new IllegalArgumentException("all tour binds should have same tour. Given tour binds have " + toursCount + " tours");

        TourBindModel tourBindModel = new TourBindModel();
        collect.forEach((tour, binds) -> {
            tourBindModel.setTour(tour);

            binds.forEach(bind -> tourBindModel.bindEmployee(bind.getId(), bind.getEmployee(), bind.getDateRange()));
        });
        return tourBindModel;
    }

    public boolean canSubmit() {
        return tour != null
                && tour.getId() != null
                && !employees.isEmpty()
                && employees.get(0).getEmployee() != null
                && employees.get(0).getEmployee().getId() != null;
    }
}
