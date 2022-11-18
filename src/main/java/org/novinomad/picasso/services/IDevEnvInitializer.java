package org.novinomad.picasso.services;

import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.novinomad.picasso.erm.entities.*;

import java.util.ArrayList;
import java.util.List;

public interface IDevEnvInitializer {

    List<Tour> createTours();

    List<Driver> createDrivers();

    List<Guide> createGuides();

    List<TourBind> createTourBindings(List<Tour> tours, List<TourParticipant> tourParticipants) throws CommonException;

    default List<TourBind> createTourBindings() throws CommonException {
        return createTourBindings(createTours(), createTourParticipants());
    }

    default List<TourParticipant> createTourParticipants(){
        List<TourParticipant> tourParticipants = new ArrayList<>();

        tourParticipants.addAll(createDrivers());
        tourParticipants.addAll(createGuides());

        return tourParticipants;
    }
}
