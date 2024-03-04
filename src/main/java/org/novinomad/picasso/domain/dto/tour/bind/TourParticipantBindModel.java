package org.novinomad.picasso.domain.dto.tour.bind;

import lombok.Data;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;

import java.util.ArrayList;
import java.util.List;

@Data
public class TourParticipantBindModel {
    private TourParticipant tourParticipant;
    private List<BindDateRange> bindIdsToDateRanges = new ArrayList<>();


    public TourParticipantBindModel() {
        bindIdsToDateRanges.add(new BindDateRange(new LocalDateTimeRange()));
    }
}
