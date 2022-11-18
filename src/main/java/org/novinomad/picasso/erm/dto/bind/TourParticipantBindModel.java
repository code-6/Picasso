package org.novinomad.picasso.erm.dto.bind;

import lombok.Data;
import org.novinomad.picasso.erm.entities.TourParticipant;

import java.util.ArrayList;
import java.util.List;

@Data
public class TourParticipantBindModel {
    private TourParticipant tourParticipant;
    private List<BindDateRange> bindIdsToDateRanges = new ArrayList<>();
}
