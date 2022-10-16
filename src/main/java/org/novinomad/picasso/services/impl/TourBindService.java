package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.dto.filters.TourBindFilter;
import org.novinomad.picasso.dto.gantt.Task;
import org.novinomad.picasso.entities.domain.impl.TourParticipant;
import org.novinomad.picasso.entities.domain.impl.Tour;
import org.novinomad.picasso.entities.domain.impl.TourBind;
import org.novinomad.picasso.exceptions.BindException;
import org.novinomad.picasso.exceptions.base.BaseException;
import org.novinomad.picasso.repositories.jpa.TourBindRepository;
import org.novinomad.picasso.services.ITourParticipantService;
import org.novinomad.picasso.services.ITourBindService;
import org.novinomad.picasso.services.ITourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.findOverlappedRange;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourBindService implements ITourBindService {

    final TourBindRepository tourBindRepository;

    final ITourService tourService;

    final ITourParticipantService tourParticipantService;

    @Autowired
    @Qualifier("tourBindJdbcRepository")
    TourBindRepository tourBindJdbcRepository;

    /**
     * Disallow bind if:
     * 1. dates to bind is out of tour dates range.
     * 2. Intersects with dates of other allTours.
     * */
    @Override
    public TourBind bind(TourParticipant tourParticipant, Tour tour, IRange dateRange) throws BaseException {
        try {
            TourBind tourBind = new TourBind(tourParticipant, tour, dateRange);

            validateBind(tourBind);

            return save(tourBind);
        } catch (BaseException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public TourBind bind(Long tourParticipantID, Long tourId, IRange dateRange) throws BaseException {
        TourParticipant tourParticipant = tourParticipantService.get(tourParticipantID)
                .orElseThrow(() -> new BaseException("TourParticipant with id: {} not found in DB", tourParticipantID));

        Tour tour = tourService.get(tourId)
                .orElseThrow(() -> new BaseException("Tour with id: {} not found in DB", tourId));

        return bind(tourParticipant, tour, dateRange);
    }

    @Override
    public void validateBind(TourBind tourBind) throws BindException {
        TourParticipant tourParticipant = tourBind.getTourParticipant();

        List<TourBind> overlapsBinds = tourBindRepository.findOverlappedBinds(tourBind.getTour().getId(), tourParticipant.getId(),
                tourBind.getStartDate(), tourBind.getEndDate());

        if (!overlapsBinds.isEmpty()) {
            Map<Tour, IRange> overlapsToursAndRanges = overlapsBinds.stream()
                    .collect(
                            Collectors.toMap(TourBind::getTour,
                                    tb -> findOverlappedRange(tourBind.getDateRange(), tb.getDateRange()))
                    );
            throw new BindException(tourParticipant, tourBind.getTour(), tourBind.getDateRange(), overlapsToursAndRanges);
        }
    }

    @Override
    public void validateBind(Long tourId, Long tourParticipantId, IRange bindRange) throws BaseException {
        List<TourBind> overlapsBinds = tourBindRepository.findOverlappedBinds(tourId, tourParticipantId, bindRange.getStartDate(), bindRange.getEndDate());

        if (!overlapsBinds.isEmpty()) {
            Map<Tour, IRange> overlapsToursAndRanges = overlapsBinds.stream()
                    .collect(Collectors.toMap(TourBind::getTour, tb -> findOverlappedRange(bindRange, tb.getDateRange())));

            TourParticipant tourParticipant = tourParticipantService.get(tourParticipantId)
                    .orElseThrow(() -> new BaseException("TourParticipant with id: {} not found in DB", tourParticipantId));

            Tour tour = tourService.get(tourId)
                    .orElseThrow(() -> new BaseException("Tour with id: {} not found in DB", tourId));

            throw new BindException(tourParticipant, tour, bindRange, overlapsToursAndRanges);
        }
    }

    @Override
    public TourBind save(TourBind tourBind) throws BaseException {
        try {
            TourBind savedTourBind = tourBindRepository.save(tourBind);
            log.debug("saved {}", tourBind);
            return savedTourBind;
        } catch (Exception e) {
            log.error("unable to save: {} because: {}", tourBind, e.getMessage(), e);
            throw new BaseException(e, "unable to save: {} because: {}", tourBind, e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<TourBind> save(Collection<TourBind> tourBinds) {
        // TODO fix for update action unique constraint violation exception. Possible variants:
        // 1. Add bind id to model.
        // 2. Fetch existent from db first, check and then save.
        List<TourBind> savedTourBinds = new ArrayList<>();
        tourBinds.forEach(tourBind -> {
            try {
                savedTourBinds.add(save(tourBind));
            } catch (BaseException ignored) {
                // ignored because save contains logging.
            }
        });
        if(savedTourBinds.size() != tourBinds.size())
            log.warn("not all TourBinds are saved. To be saved: {} saved: {}", tourBinds.size(), savedTourBinds.size());

        return savedTourBinds;
    }

    @Override
    public void delete(Long id) throws BaseException {
        try {
            tourBindRepository.deleteById(id);
        } catch (Exception e) {
            log.error("unable to delete TourBind with id: {} because: {}", id, e.getMessage(), e);
            throw new BaseException(e, "unable to delete TourBind with id: {} because: {}", id, e.getMessage());
        }
    }

    @Transactional
    public List<Long> delete(Collection<Long> ids) {
        List<Long> deletedTourBindIds = new ArrayList<>();

        ids.forEach(id -> {
            try {
                delete(id);
                deletedTourBindIds.add(id);
            } catch (BaseException ignored) {
                // ignored because save contains logging.
            }
        });
        if(deletedTourBindIds.size() != ids.size())
            log.warn("not all TourBinds are deleted. To be deleted: {} deleted: {}", deletedTourBindIds.size(), ids.size());

        return deletedTourBindIds;
    }

    @Transactional
    public List<Long> deleteAll(Collection<TourBind> tourBinds) {
        List<Long> ids = tourBinds.stream().map(TourBind::getId).toList();
        return delete(ids);
    }

    @Override
    public Optional<TourBind> get(Long id) {
        try {
            return tourBindRepository.findById(id);
        } catch (Exception e) {
            log.error("unable to get TourBind by id: {} because: {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<TourBind> get() {
        return tourBindRepository.findAll();
    }

    @Override
    public List<TourBind> get(TourBindFilter tourBindFilter) {
        List<Long> tourIds = tourBindFilter.getTourIds().isEmpty() ? null : tourBindFilter.getTourIds();
        List<Long> tourParticipantIds = tourBindFilter.getTourParticipantIds().isEmpty() ? null : tourBindFilter.getTourParticipantIds();

        return tourBindJdbcRepository.findByFilter(tourBindFilter.getStartDate(), tourBindFilter.getEndDate(), tourIds, tourParticipantIds);
    }

    @Override
    public List<Task> getForGanttChart(TourBindFilter tourBindFilter) {
        if (tourBindFilter == null)
            tourBindFilter = new TourBindFilter();

        return Task.fromBindsWithChildrenInList(get(tourBindFilter));
    }
}
