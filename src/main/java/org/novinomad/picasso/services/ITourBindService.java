package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.dto.filters.TourBindFilter;
import org.novinomad.picasso.entities.domain.impl.Employee;
import org.novinomad.picasso.entities.domain.impl.Tour;
import org.novinomad.picasso.entities.domain.impl.TourBind;
import org.novinomad.picasso.dto.gantt.Task;
import org.novinomad.picasso.exceptions.BindException;
import org.novinomad.picasso.exceptions.base.PicassoException;

import java.util.Collection;
import java.util.List;

public interface ITourBindService extends ICrud<TourBind> {

    TourBind bind(Employee employee, Tour tour, IRange dateRange) throws PicassoException;
    TourBind bind(Long employeeID, Long tourId, IRange dateRange) throws PicassoException;
    default TourBind bind(TourBind tourBind) throws PicassoException {
        return bind(tourBind.getEmployee(), tourBind.getTour(), tourBind.getDateRange());
    }
    void validateBind(Long tourId, Long employeeId, IRange bindRange) throws PicassoException;
    void validateBind(TourBind tourBind) throws BindException;
    default boolean overlapsWithOtherTour(TourBind tourBind) {
        try {
            validateBind(tourBind);
            return false;
        } catch (BindException e) {
            return true;
        }
    }

    List<TourBind> get(TourBindFilter tourBindFilter);

    List<TourBind> save(Collection<TourBind> tourBinds);

    List<Task> getForGanttChart(TourBindFilter tourBindFilter);
}
