package org.novinomad.picasso.services.impl;

import com.sun.xml.bind.v2.TODO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.dto.filters.TourCriteria;
import org.novinomad.picasso.dto.gantt.Task;
import org.novinomad.picasso.exceptions.BindException;
import org.novinomad.picasso.exceptions.base.PicassoException;
import org.novinomad.picasso.repositories.jpa.TourBindRepository;
import org.novinomad.picasso.services.IEmployeeService;
import org.novinomad.picasso.services.ITourBindService;
import org.novinomad.picasso.services.ITourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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

    final IEmployeeService employeeService;

    @Autowired
    @Qualifier("tourBindJdbcRepository")
    TourBindRepository tourBindJdbcRepository;

    /**
     * Disallow bind if:
     * 1. dates to bind is out of tour dates range.
     * 2. Intersects with dates of other allTours.
     * */
    @Override
    public TourBind bind(Employee employee, Tour tour, LocalDateTime startDate, LocalDateTime endDate) throws PicassoException {
        try {
            TourBind tourBind = new TourBind(employee, tour, startDate, endDate);

            validateBind(tourBind);

            return save(tourBind);
        } catch (PicassoException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public TourBind bind(Long employeeID, Long tourId, LocalDateTime startDate, LocalDateTime endDate) throws PicassoException {
        Employee employee = employeeService.get(employeeID)
                .orElseThrow(() -> new PicassoException("Employee with id: {} not found in DB", employeeID));

        Tour tour = tourService.get(tourId)
                .orElseThrow(() -> new PicassoException("Tour with id: {} not found in DB", tourId));

        return bind(employee, tour, startDate, endDate);
    }

    @Override
    public void validateBind(TourBind tourBind) throws BindException {
        Employee employee = tourBind.getEmployee();

        List<TourBind> overlapsBinds = tourBindRepository.findOverlappedBinds(tourBind.getTour().getId(), employee.getId(),
                tourBind.getStartDate(), tourBind.getEndDate());

        if (!overlapsBinds.isEmpty()) {
            Map<Tour, LocalDateTimeRange> overlapsToursAndRanges = overlapsBinds.stream()
                    .collect(
                            Collectors.toMap(TourBind::getTour,
                                    tb -> findOverlappedRange(tourBind.getDateRange(), tb.getDateRange()))
                    );
            throw new BindException(employee, tourBind.getTour(), tourBind.getDateRange(), overlapsToursAndRanges);
        }
    }

    @Override
    public void validateBind(Long tourId, Long employeeId, LocalDateTimeRange bindRange) throws PicassoException {
        List<TourBind> overlapsBinds = tourBindRepository.findOverlappedBinds(tourId, employeeId, bindRange.getStartDate(), bindRange.getEndDate());

        if (!overlapsBinds.isEmpty()) {
            Map<Tour, LocalDateTimeRange> overlapsToursAndRanges = overlapsBinds.stream()
                    .collect(Collectors.toMap(TourBind::getTour, tb -> findOverlappedRange(bindRange, tb.getDateRange())));

            Employee employee = employeeService.get(employeeId)
                    .orElseThrow(() -> new PicassoException("Employee with id: {} not found in DB", employeeId));

            Tour tour = tourService.get(tourId)
                    .orElseThrow(() -> new PicassoException("Tour with id: {} not found in DB", tourId));

            throw new BindException(employee, tour, bindRange, overlapsToursAndRanges);
        }
    }

    @Override
    public TourBind save(TourBind tourBind) throws PicassoException {
        try {
            TourBind savedTourBind = tourBindRepository.save(tourBind);
            log.debug("saved {}", tourBind);
            return savedTourBind;
        } catch (Exception e) {
            log.error("unable to save: {} because: {}", tourBind, e.getMessage(), e);
            throw new PicassoException(e, "unable to save: {} because: {}", tourBind, e.getMessage());
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
            } catch (PicassoException ignored) {
                // ignored because save contains logging.
            }
        });
        if(savedTourBinds.size() != tourBinds.size())
            log.warn("not all TourBinds are saved. To be saved: {} saved: {}", tourBinds.size(), savedTourBinds.size());

        return savedTourBinds;
    }

    @Override
    public void delete(Long id) throws PicassoException {
        try {
            tourBindRepository.deleteById(id);
        } catch (Exception e) {
            log.error("unable to delete TourBind with id: {} because: {}", id, e.getMessage(), e);
            throw new PicassoException(e, "unable to delete TourBind with id: {} because: {}", id, e.getMessage());
        }
    }

    @Transactional
    public List<Long> delete(Collection<Long> ids) {
        List<Long> deletedTourBindIds = new ArrayList<>();

        ids.forEach(id -> {
            try {
                delete(id);
                deletedTourBindIds.add(id);
            } catch (PicassoException ignored) {
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
    public List<TourBind> get(TourCriteria tourCriteria) {
        List<Long> tourIds = tourCriteria.getTourIds().isEmpty() ? null : tourCriteria.getTourIds();
        List<Long> employeeIds = tourCriteria.getEmployeeIds().isEmpty() ? null : tourCriteria.getEmployeeIds();

        return tourBindJdbcRepository.findByFilter(tourCriteria.getStartDate(), tourCriteria.getEndDate(), tourIds, employeeIds);
    }

    @Override
    public List<Task> getForGanttChart(TourCriteria tourCriteria) {
        if (tourCriteria == null)
            tourCriteria = new TourCriteria();

        return Task.fromBindsWithChildrenInList(get(tourCriteria));
    }
}
