package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.entities.domain.impl.Guide;
import org.novinomad.picasso.entities.domain.impl.TourParticipant;
import org.novinomad.picasso.exceptions.base.BaseException;
import org.novinomad.picasso.repositories.jpa.TourParticipantRepository;
import org.novinomad.picasso.services.ITourParticipantService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service("tourParticipantService")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourParticipantService implements ITourParticipantService {

    final TourParticipantRepository tourParticipantRepository;

    @Override
    public <T extends TourParticipant> T save(T tourParticipant) throws BaseException {
        try {
            T savedTourParticipant = tourParticipantRepository.save(tourParticipant);
            log.debug("saved {}", tourParticipant);
            return savedTourParticipant;
        } catch (Exception e) {
            log.error("unable to create: {} because: {}", tourParticipant, e.getMessage(), e);
            throw new BaseException(e, "unable to create: {} because: {}", tourParticipant, e.getMessage());
        }
    }

    @Transactional
    public List<TourParticipant> save(Collection<TourParticipant> tourParticipants) {
        List<TourParticipant> savedTourParticipants = new ArrayList<>();
        tourParticipants.forEach(tourParticipant -> {
            try {
                savedTourParticipants.add(save(tourParticipant));
            } catch (BaseException ignored) {
                // ignored because save contains logging.
            }
        });
        if (savedTourParticipants.size() != tourParticipants.size())
            log.warn("not all TourParticipants are saved. To be saved: {} saved: {}", tourParticipants.size(), savedTourParticipants.size());

        return savedTourParticipants;
    }

    @Override
    public void delete(Long id) throws BaseException {
        try {
            tourParticipantRepository.deleteById(id);
        } catch (Exception e) {
            log.error("unable to delete TourParticipant with id: {} because: {}", id, e.getMessage(), e);
            throw new BaseException(e, "unable to delete TourParticipant with id: {} because: {}", id, e.getMessage());
        }
    }

    @Transactional
    public List<Long> delete(Collection<Long> ids) {
        List<Long> deletedTourParticipants = new ArrayList<>();

        ids.forEach(id -> {
            try {
                delete(id);
                deletedTourParticipants.add(id);
            } catch (BaseException ignored) {
                // ignored because save contains logging.
            }
        });
        if (deletedTourParticipants.size() != ids.size())
            log.warn("not all TourParticipants are deleted. To be deleted: {} deleted: {}", deletedTourParticipants.size(), ids.size());

        return deletedTourParticipants;
    }

    @Transactional
    public List<Long> deleteAll(Collection<TourParticipant> tourParticipants) {
        List<Long> ids = tourParticipants.stream().map(TourParticipant::getId).toList();
        return delete(ids);
    }

    @Override
    public Optional<TourParticipant> get(Long id) {
        try {
            return tourParticipantRepository.findById(id);
        } catch (Exception e) {
            log.error("unable to get TourParticipant by id: {} because: {}", id, e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public List<TourParticipant> get() {
        return tourParticipantRepository.findAll();
    }

    @Override
    public List<TourParticipant> get(Long... ids) {
        return tourParticipantRepository.findAllByIdIn(Arrays.asList(ids));
    }

    @Override
    public <T extends TourParticipant> List<T> get(TourParticipant.Type tourParticipantType) {
        return tourParticipantRepository.findAllByType(tourParticipantType);
    }

    @Override
    public List get(String className) {
        List<Guide> allByClass = tourParticipantRepository.findAllByClass(Guide.class.getName());
        return allByClass;
    }

    @Override
    public List<TourParticipant> get(List<TourParticipant.Type> types) {
        return tourParticipantRepository.findAllByTypeIn(types);
    }

    @Override
    public List<TourParticipant.Type> getTourParticipantTypes() {
        return Arrays.asList(TourParticipant.Type.values());
    }
}
