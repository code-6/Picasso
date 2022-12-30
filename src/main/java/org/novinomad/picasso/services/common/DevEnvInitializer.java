package org.novinomad.picasso.services.common;

import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.novinomad.picasso.domain.erm.entities.auth.User;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.domain.erm.entities.tour.TourBind;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Driver;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Guide;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;

import java.util.ArrayList;
import java.util.List;

public interface DevEnvInitializer {

    List<User> createUsers();

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
