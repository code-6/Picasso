package org.novinomad.picasso.dto.bind;

import lombok.Data;
import org.novinomad.picasso.entities.domain.impl.TourParticipant;

import java.util.ArrayList;
import java.util.List;

@Data
public class TourParticipantBindModel {
    private TourParticipant tourParticipant;
    private List<BindDateRange> bindIdsToDateRanges = new ArrayList<>();
}
