package org.novinomad.picasso.services;

import org.novinomad.picasso.entities.domain.impl.TourParticipant;
import org.novinomad.picasso.exceptions.base.BaseException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface ITourParticipantService {

    default List<TourParticipant> get(TourParticipant.Type... types) {
        return get(Arrays.asList(types));
    }

    <T extends TourParticipant> T save(T tourParticipant) throws BaseException;

    void delete(Long id) throws BaseException;

    Optional<TourParticipant> get(Long id);

    List<TourParticipant> get();

    List<TourParticipant> get(Long... ids);

    <T extends TourParticipant> List<T> get(TourParticipant.Type tourParticipantType);

    default List<TourParticipant> getOrEmpty(TourParticipant.Type... types) {
        if(types == null || types.length == 0)
            return Collections.emptyList();

        return get(Arrays.asList(types));
    }

    default List<TourParticipant> getOrEmpty(List<TourParticipant.Type> types) {
        if(types == null || types.isEmpty())
            return Collections.emptyList();

        return get(types);
    }

    <T extends TourParticipant> List<T> get(String className);

    List<TourParticipant> get(List<TourParticipant.Type> types);

    List<TourParticipant.Type> getTourParticipantTypes();
}
