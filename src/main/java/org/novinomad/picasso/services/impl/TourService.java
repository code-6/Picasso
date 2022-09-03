package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.dto.filters.TourFilter;
import org.novinomad.picasso.exceptions.base.PicassoException;
import org.novinomad.picasso.repositories.jpa.TourRepository;
import org.novinomad.picasso.services.ITourService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourService implements ITourService {
    
    final TourRepository tourRepository;
    
    @Override
    public Tour save(Tour tour) throws PicassoException {
        try {
            Tour savedTour = tourRepository.save(tour);
            log.debug("saved {}", tour);
            return savedTour;
        } catch (Exception e) {
            log.error("unable to create: {} because: {}", tour, e.getMessage(), e);
            throw new PicassoException(e, "unable to create: {} because: {}", tour, e.getMessage());
        }
    }

    @Transactional
    public List<Tour> save(Collection<Tour> tours) {
        List<Tour> savedTours = new ArrayList<>();
        tours.forEach(tour -> {
            try {
                savedTours.add(save(tour));
            } catch (PicassoException ignored) {
                // ignored because save contains logging.
            }
        });
        if(savedTours.size() != tours.size())
            log.warn("not all Tours are saved. To be saved: {} saved: {}", tours.size(), savedTours.size());

        return savedTours;
    }

    @Override
    public void delete(Long id) throws PicassoException {
        try {
            tourRepository.deleteById(id);
        } catch (Exception e) {
            log.error("unable to delete Tour with id: {} because: {}", id, e.getMessage(), e);
            throw new PicassoException(e, "unable to delete Tour with id: {} because: {}", id, e.getMessage());
        }
    }

    @Transactional
    public List<Long> delete(Collection<Long> ids) {
        List<Long> deletedTours = new ArrayList<>();

        ids.forEach(id -> {
            try {
                delete(id);
                deletedTours.add(id);
            } catch (PicassoException ignored) {
                // ignored because save contains logging.
            }
        });
        if(deletedTours.size() != ids.size())
            log.warn("not all Tours are deleted. To be deleted: {} deleted: {}", deletedTours.size(), ids.size());

        return deletedTours;
    }

    @Transactional
    public List<Long> deleteAll(Collection<Tour> tours) {
        List<Long> ids = tours.stream().map(Tour::getId).toList();
        return delete(ids);
    }

    @Override
    public Optional<Tour> get(Long id) {
        try {
            return tourRepository.findById(id);
        } catch (Exception e) {
            log.error("unable to get Tour by id: {} because: {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<Tour> get() {
        return tourRepository.findAll();
    }

    @Override
    public List<Tour> get(TourFilter tourFilter) {
        try {
            return tourRepository.findByFilter(tourFilter.getStartDate(), tourFilter.getEndDate(), tourFilter.getTourIds().isEmpty() ? null : tourFilter.getTourIds());
        } catch (Exception e) {
            log.error("unable to get Tours by filter: {} because: {}", tourFilter, e.getMessage(), e);
            throw e;
        }
    }
}
