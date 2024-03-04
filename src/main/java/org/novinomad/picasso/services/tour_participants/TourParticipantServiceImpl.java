package org.novinomad.picasso.services.tour_participants;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.repositories.jpa.TourParticipantRepository;
import org.novinomad.picasso.services.AbstractCrudCacheService;
import org.slf4j.event.Level;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("tourParticipantService")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Loggable(level = Level.ERROR)
public class TourParticipantServiceImpl extends AbstractCrudCacheService<Long, TourParticipant> implements TourParticipantService {


    private final TourParticipantRepository tourParticipantRepository;

    public TourParticipantServiceImpl(TourParticipantRepository tourParticipantRepository) {
        super(tourParticipantRepository);
        this.tourParticipantRepository = tourParticipantRepository;
    }

    @Override
    public List<TourParticipant> get() {
        return super.get().stream()
                .sorted()
                .toList();
    }

    @Override
    public List<TourParticipant> get(Collection<TourParticipant.Type> tourParticipantTypes) {
        if(CollectionUtils.isEmpty(tourParticipantTypes)) {
            return get();
        } else {
            List<TourParticipant> tourParticipants;
            if(CACHE.asMap().isEmpty()) {
                tourParticipants = get();
            } else {
                tourParticipants = new ArrayList<>(CACHE.asMap().values());
            }
            return tourParticipants.stream().filter(tp -> tourParticipantTypes.contains(tp.getType())).toList();
//            return tourParticipantRepository.findAllByTypeIn(tourParticipantTypes);
        }
    }
}
