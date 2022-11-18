package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.erm.entities.TourParticipant;
import org.novinomad.picasso.commons.exceptions.base.CommonException;

import java.util.*;

public interface ITourParticipantService extends ICrud<Long, TourParticipant> {

    List<TourParticipant> get(Collection<TourParticipant.Type> tourParticipantTypes);

    default List<TourParticipant> getOrEmpty(TourParticipant.Type... types) {
        if(types == null || types.length == 0)
            return Collections.emptyList();

        return get(Arrays.asList(types));
    }
}
