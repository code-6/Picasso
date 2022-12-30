package org.novinomad.picasso.services.tour;

import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.commons.Crud;
import org.novinomad.picasso.commons.ILocalDateTimeRange;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.commons.enums.CRUD;
import org.novinomad.picasso.commons.exceptions.CRUDException;
import org.novinomad.picasso.domain.dto.tour.bind.TourBindModel;
import org.novinomad.picasso.domain.dto.tour.filters.TourBindFilter;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.domain.erm.entities.tour.TourBind;
import org.novinomad.picasso.domain.dto.tour.gantt.Task;
import org.novinomad.picasso.commons.exceptions.BindException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;

import java.util.Collection;
import java.util.List;

public interface TourBindService extends Crud<Long, TourBind> {

    TourBind bind(TourParticipant tourParticipant, Tour tour, LocalDateTimeRange dateRange) throws BindException;
    TourBind bind(Long tourParticipantId, Long tourId, LocalDateTimeRange dateRange) throws BindException;

    default List<TourBind> bind(TourBindModel tourBind) throws BindException {
        return save(tourBind.toEntities());
    }

    @Override
    default TourBind save(TourBind tourBind) {
        try {
            return bind(tourBind.getTourParticipant(), tourBind.getTour(), tourBind.getDateTimeRange());
        } catch (Exception e) {
            throw new CRUDException(e, tourBind.getId() == null ? CRUD.CREATE : CRUD.UPDATE, tourBind);
        }
    }

    void validateBind(Long tourId, Long tourParticipantId, LocalDateTimeRange bindRange) throws BindException;
    void validateBind(TourBind tourBind) throws BindException;
    default boolean overlapsWithOtherTour(TourBind tourBind) {
        try {
            validateBind(tourBind);
            return false;
        } catch (BindException e) {
            return true;
        }
    }

    List<TourBind> findOverlappedBinds(Long tourId, Long tourParticipantId, LocalDateTimeRange newBindDateRange);

    List<TourBind> get(TourBindFilter tourBindFilter);

    List<Task> getForGanttChart(TourBindFilter tourBindFilter);
}
