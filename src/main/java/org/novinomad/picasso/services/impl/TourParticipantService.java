package org.novinomad.picasso.services.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.erm.entities.Guide;
import org.novinomad.picasso.erm.entities.TourParticipant;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.repositories.jpa.TourParticipantRepository;
import org.novinomad.picasso.services.ITourParticipantService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service("tourParticipantService")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourParticipantService implements ITourParticipantService {

    final TourParticipantRepository tourParticipantRepository;

    @Override
    public TourParticipant save(TourParticipant tourParticipant) throws CommonRuntimeException {
        try {
            return tourParticipantRepository.save(tourParticipant);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to create {} because {}", tourParticipant, e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            tourParticipantRepository.deleteById(id);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete tour participant with id {} because {}", id, e.getMessage());
        }
    }

    @Override
    public void deleteById(Iterable<Long> ids) throws CommonRuntimeException {
        try {
            tourParticipantRepository.deleteAllById(ids);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete tour participants with ids {} because {}", ids, e.getMessage());
        }
    }

    /**
     * @implNote If tourParticipants not provided wil be removed all items. So be careful when using this method
     * */
    @Override
    public void delete(TourParticipant... tourParticipants) throws CommonRuntimeException {
        try {
            if(tourParticipants == null || tourParticipants.length == 0) {
                tourParticipantRepository.deleteAll();
            } else {
                tourParticipantRepository.deleteAll(Arrays.asList(tourParticipants));
            }
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to delete tour participants {} because {}", tourParticipants, e.getMessage());
        }
    }

    /**
     * @implNote If ids not provided wil be returned all items.
     * */
    @Override
    public List<TourParticipant> get(Long... ids) throws CommonRuntimeException {
        try {
            if(ids == null || ids.length == 0) {
                return tourParticipantRepository.findAll();
            } else {
                return tourParticipantRepository.findAllById(Arrays.asList(ids));
            }
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get tour participants by ids {} because {}", ids, e.getMessage());
        }
    }

    @Override
    public Optional<TourParticipant> get(Long id) throws CommonRuntimeException {
        try {
            return tourParticipantRepository.findById(id);
        } catch (Exception e) {
            throw new CommonRuntimeException(e, "unable to get tour participant by id {} because {}", id, e.getMessage());
        }
    }

    @Override
    public List<TourParticipant> get(Collection<TourParticipant.Type> tourParticipantTypes) {
        if(CollectionUtils.isEmpty(tourParticipantTypes)) {
            return tourParticipantRepository.findAll();
        } else {
            return tourParticipantRepository.findAllByTypeIn(tourParticipantTypes);
        }
    }
}
