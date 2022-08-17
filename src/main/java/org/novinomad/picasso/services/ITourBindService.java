package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.dto.filters.TourCriteria;
import org.novinomad.picasso.dto.gantt.Task;
import org.novinomad.picasso.exceptions.BindException;
import org.novinomad.picasso.exceptions.base.PicassoException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface ITourBindService extends ICrud<TourBind> {

    TourBind bind(Employee employee, Tour tour, LocalDateTime startDate, LocalDateTime endDate) throws PicassoException;
    TourBind bind(Long employeeID, Long tourId, LocalDateTime startDate, LocalDateTime endDate) throws PicassoException;
    default TourBind bind(TourBind tourBind) throws PicassoException {
        return bind(tourBind.getEmployee(), tourBind.getTour(), tourBind.getStartDate(), tourBind.getEndDate());
    }
    void validateBind(Long tourId, Long employeeId, LocalDateTimeRange bindRange) throws PicassoException;
    void validateBind(TourBind tourBind) throws BindException;
    default boolean overlapsWithOtherTour(TourBind tourBind) {
        try {
            validateBind(tourBind);
            return false;
        } catch (BindException e) {
            return true;
        }
    }

    List<TourBind> get(TourCriteria tourCriteria);

    List<TourBind> save(Collection<TourBind> tourBinds);

    List<Task> getForGanttChart(TourCriteria tourCriteria);
}
