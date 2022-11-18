package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.erm.dto.filters.TourBindFilter;
import org.novinomad.picasso.erm.entities.TourParticipant;
import org.novinomad.picasso.erm.entities.Tour;
import org.novinomad.picasso.erm.entities.TourBind;
import org.novinomad.picasso.erm.dto.gantt.Task;
import org.novinomad.picasso.commons.exceptions.BindException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ITourBindService extends ICrud<Long, TourBind> {

    TourBind bind(TourParticipant tourParticipant, Tour tour, IRange dateRange) throws BindException;
    TourBind bind(Long tourParticipantId, Long tourId, IRange dateRange) throws BindException;

    @Override
    default TourBind save(TourBind tourBind) {
        try {
            return bind(tourBind.getTourParticipant(), tourBind.getTour(), tourBind.getDateRange());
        } catch (BindException e) {
            throw new CommonRuntimeException(e);
        }
    }

    void validateBind(Long tourId, Long tourParticipantId, IRange bindRange) throws BindException;
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

    List<Task> getForGanttChart(TourBindFilter tourBindFilter);
}
