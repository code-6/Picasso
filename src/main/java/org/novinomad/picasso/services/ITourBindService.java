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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ITourBindService {

    TourBind bind(TourParticipant tourParticipant, Tour tour, IRange dateRange) throws BaseException;
    TourBind bind(Long tourParticipantID, Long tourId, IRange dateRange) throws BaseException;
    default TourBind bind(TourBind tourBind) throws BaseException {
        return bind(tourBind.getTourParticipant(), tourBind.getTour(), tourBind.getDateRange());
    }

    @Transactional
    default List<TourBind> bind(Collection<TourBind> tourBinds) throws BaseException {
        List<TourBind> savedBinds = new ArrayList<>();
        for (TourBind tourBind : tourBinds) {
            savedBinds.add(bind(tourBind));
        }
        return savedBinds;
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

    void delete(Long id) throws BaseException;

    @Transactional
    List<Long> delete(Collection<Long> ids);

    Optional<TourBind> get(Long id);

    List<TourBind> get();

    List<TourBind> get(TourBindFilter tourBindFilter);

    List<Task> getForGanttChart(TourBindFilter tourBindFilter);
}
