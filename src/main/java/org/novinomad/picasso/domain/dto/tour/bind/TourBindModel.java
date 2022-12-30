package org.novinomad.picasso.domain.dto.tour.bind;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.novinomad.picasso.commons.ILocalDateTimeRange;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.domain.erm.entities.tour.TourBind;
import org.novinomad.picasso.commons.exceptions.BindException;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class TourBindModel{

    private Tour tour = new Tour();

    private List<TourParticipantBindModel> tourParticipants = new ArrayList<>();

    public TourBindModel(Tour tour) {
        this.tour = tour;
    }

    public TourBindModel bindTourParticipant(TourParticipant tourParticipant) {
        if (tour != null) {
            bindTourParticipant(tourParticipant, tour.getDateTimeRange());
        }
        return this;
    }

    public TourBindModel bindTourParticipant(TourParticipant tourParticipant, LocalDateTimeRange dateRange) {
        if (tour != null) {
            bindTourParticipant(null, tourParticipant, dateRange == null ? tour.getDateTimeRange() : dateRange);
        }
        return this;
    }

    public TourBindModel bindTourParticipant(Long bindId, TourParticipant tourParticipant, LocalDateTimeRange dateRange) {
        getBoundTourParticipant(tourParticipant).ifPresentOrElse(tourParticipantBindModel -> {
            List<BindDateRange> dateRanges = tourParticipantBindModel.getBindIdsToDateRanges();
            if (dateRanges.stream().noneMatch(bindDateRange -> bindDateRange.getDateTimeRange().equals(dateRange)))
                dateRanges.add(new BindDateRange(bindId, dateRange));
        }, () -> {
            TourParticipantBindModel tourParticipantBindModel = new TourParticipantBindModel();
            tourParticipantBindModel.getBindIdsToDateRanges().add(new BindDateRange(bindId, dateRange));
            tourParticipantBindModel.setTourParticipant(tourParticipant);
            tourParticipants.add(tourParticipantBindModel);
        });
        return this;
    }

    public Optional<TourParticipantBindModel> getBoundTourParticipant(TourParticipant tourParticipant) {
        return tourParticipants.isEmpty()
                ? Optional.empty()
                : tourParticipants.stream()
                .filter(e -> e != null && e.getTourParticipant() != null && e.getTourParticipant().equals(tourParticipant))
                .findAny();
    }

    public List<TourBind> toEntities() throws BindException {
        List<TourBind> tourBinds = new ArrayList<>();

        if (tourParticipants.isEmpty()) tourBinds.add(new TourBind(null, tour, tour.getDateTimeRange()));
        else
            for (TourParticipantBindModel tourParticipantBindModel : tourParticipants)
                for (BindDateRange bindDateRange : tourParticipantBindModel.getBindIdsToDateRanges())
                    tourBinds.add(new TourBind(bindDateRange.getBindId(), tourParticipantBindModel.getTourParticipant(), tour, bindDateRange.getDateTimeRange()));

        return tourBinds;
    }

    public static TourBindModel fromEntities(List<TourBind> tourBinds) {
        Map<Tour, List<TourBind>> collect = tourBinds.stream().collect(Collectors.groupingBy(TourBind::getTour));
        int toursCount = collect.keySet().size();
        if (toursCount > 1)
            throw new IllegalArgumentException("all tour binds should have same tour. Given tour binds have " + toursCount + " tours");

        TourBindModel tourBindModel = new TourBindModel();
        collect.forEach((tour, binds) -> {
            tourBindModel.setTour(tour);

            binds.forEach(bind -> tourBindModel.bindTourParticipant(bind.getId(), bind.getTourParticipant(), bind.getDateTimeRange()));
        });
        return tourBindModel;
    }

    public boolean canSubmit() {
        return tour != null
                && tour.getId() != null
                && !tourParticipants.isEmpty()
                && tourParticipants.get(0).getTourParticipant() != null
                && tourParticipants.get(0).getTourParticipant().getId() != null;
    }
}
