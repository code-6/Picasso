package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.commons.exceptions.BindException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.erm.dto.filters.TourBindFilter;
import org.novinomad.picasso.erm.dto.gantt.Task;
import org.novinomad.picasso.erm.entities.Tour;
import org.novinomad.picasso.erm.entities.TourBind;
import org.novinomad.picasso.erm.entities.TourParticipant;
import org.novinomad.picasso.repositories.TourBindRepository;
import org.novinomad.picasso.services.ITourBindService;
import org.novinomad.picasso.services.ITourParticipantService;
import org.novinomad.picasso.services.ITourService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.findOverlappedRange;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourBindService implements ITourBindService {


    private final TourBindRepository tourBindRepository;

    private final ITourService tourService;

    private final ITourParticipantService tourParticipantService;

    public TourBindService(@Qualifier("tourBindRepository") TourBindRepository tourBindRepository,
                           ITourService tourService,
                           ITourParticipantService tourParticipantService) {

        this.tourBindRepository = tourBindRepository;
        this.tourService = tourService;
        this.tourParticipantService = tourParticipantService;
    }

    /**
     * Disallow bind if:
     * 1. dates to bind is out of tour dates range.
     * 2. Intersects with dates of other tours.
     * */
    @Override
    public TourBind bind(TourParticipant tourParticipant, Tour tour, IRange dateRange) throws CommonRuntimeException, BindException {
        try {
            TourBind tourBind = new TourBind(tourParticipant, tour, dateRange);

            validateBind(tourBind);

            return tourBindRepository.save(tourBind);
        } catch (CommonRuntimeException | BindException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public TourBind bind(Long tourParticipantID, Long tourId, IRange dateRange) throws CommonRuntimeException, BindException {
        TourParticipant tourParticipant = tourParticipantService.get(tourParticipantID)
                .orElseThrow(() -> new CommonRuntimeException("TourParticipant with id: {} not found in DB", tourParticipantID));

        Tour tour = tourService.get(tourId)
                .orElseThrow(() -> new CommonRuntimeException("Tour with id: {} not found in DB", tourId));

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
    public void validateBind(Long tourId, Long tourParticipantId, IRange bindRange) throws CommonRuntimeException, BindException {
        List<TourBind> overlapsBinds = tourBindRepository.findOverlappedBinds(tourId, tourParticipantId, bindRange.getStartDate(), bindRange.getEndDate());

        if (!overlapsBinds.isEmpty()) {
            Map<Tour, IRange> overlapsToursAndRanges = overlapsBinds.stream()
                    .collect(Collectors.toMap(TourBind::getTour, tb -> findOverlappedRange(bindRange, tb.getDateRange())));

            TourParticipant tourParticipant = tourParticipantService.get(tourParticipantId)
                    .orElseThrow(() -> new CommonRuntimeException("TourParticipant with id: {} not found in DB", tourParticipantId));

            Tour tour = tourService.get(tourId)
                    .orElseThrow(() -> new CommonRuntimeException("Tour with id: {} not found in DB", tourId));

            throw new BindException(tourParticipant, tour, bindRange, overlapsToursAndRanges);
        }
    }

    @Override
    public List<TourBind> get(TourBindFilter tourBindFilter) {
        List<Long> tourIds = tourBindFilter.getTourIds().isEmpty() ? null : tourBindFilter.getTourIds();
        List<Long> tourParticipantIds = tourBindFilter.getTourParticipantIds().isEmpty() ? null : tourBindFilter.getTourParticipantIds();

        return tourBindRepository.findByFilter(tourBindFilter.getStartDate(), tourBindFilter.getEndDate(), tourIds, tourParticipantIds);
    }

    @Override
    public List<Task> getForGanttChart(TourBindFilter tourBindFilter) {
        if (tourBindFilter == null)
            tourBindFilter = new TourBindFilter();

        return Task.fromBindsWithChildrenInList(get(tourBindFilter));
    }

    @Override
    public void deleteById(Long id) {
        try {
            tourBindRepository.deleteById(id);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete bind with id {} because {}", id, e.getMessage());
        }
    }

    @Override
    public void deleteById(Iterable<Long> ids) throws CommonRuntimeException {
        try {
            tourBindRepository.deleteAllById(ids);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete binds with ids {} because {}", ids, e.getMessage());
        }
    }

    @Override
    public void delete(TourBind... tourBinds) throws CommonRuntimeException {
        try {
            if(tourBinds == null || tourBinds.length == 0) {
                tourBindRepository.deleteAll();
            } else {
                tourBindRepository.deleteAll(Arrays.asList(tourBinds));
            }
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete binds {} because {}", tourBinds, e.getMessage());
        }
    }

    @Override
    public List<TourBind> get(Long... ids) throws CommonRuntimeException {
        try {
            if(ids == null || ids.length == 0) {
                return tourBindRepository.findAll();
            } else {
                return tourBindRepository.findAllById(Arrays.asList(ids));
            }
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get binds by ids {} because {}", ids, e.getMessage());
        }
    }

    @Override
    public Optional<TourBind> get(Long id) throws CommonRuntimeException {
        try {
            return tourBindRepository.findById(id);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get bind by id {} because {}", id, e.getMessage());
        }
    }
}
