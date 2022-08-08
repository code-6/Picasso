package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.dto.filters.TourBindFilter;
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
    void checkForTourOverlaps(TourBind tourBind) throws BindException;
    default boolean overlapsWithOtherTour(TourBind tourBind) {
        try {
            checkForTourOverlaps(tourBind);
            return false;
        } catch (BindException e) {
            return true;
        }
    }
    List<TourBind> get(TourBindFilter tourBindFilter);

    List<TourBind> save(Collection<TourBind> tourBinds);
}
