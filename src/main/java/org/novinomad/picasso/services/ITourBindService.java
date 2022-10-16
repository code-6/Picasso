package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.dto.filters.TourBindFilter;
import org.novinomad.picasso.entities.domain.impl.TourParticipant;
import org.novinomad.picasso.entities.domain.impl.Tour;
import org.novinomad.picasso.entities.domain.impl.TourBind;
import org.novinomad.picasso.dto.gantt.Task;
import org.novinomad.picasso.exceptions.BindException;
import org.novinomad.picasso.exceptions.base.BaseException;

import java.util.Collection;
import java.util.List;

public interface ITourBindService extends ICrud<TourBind> {

    TourBind bind(TourParticipant tourParticipant, Tour tour, IRange dateRange) throws BaseException;
    TourBind bind(Long tourParticipantID, Long tourId, IRange dateRange) throws BaseException;
    default TourBind bind(TourBind tourBind) throws BaseException {
        return bind(tourBind.getTourParticipant(), tourBind.getTour(), tourBind.getDateRange());
    }
    void validateBind(Long tourId, Long tourParticipantId, IRange bindRange) throws BaseException;
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
