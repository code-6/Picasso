package org.novinomad.picasso.services.tour_participants;

import org.novinomad.picasso.commons.Crud;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;

import javax.transaction.Transactional;
import java.util.*;

public interface TourParticipantService extends Crud<Long, TourParticipant> {

    List<TourParticipant> get(Collection<TourParticipant.Type> tourParticipantTypes);

    default List<TourParticipant> getOrEmpty(TourParticipant.Type... types) {
        if(types == null || types.length == 0)
            return Collections.emptyList();

        return get(Arrays.asList(types));
    }

    @Transactional
    void deleteSoft(Long id);

    @Transactional
    void deleteSoft(Collection<Long> ids);
}
