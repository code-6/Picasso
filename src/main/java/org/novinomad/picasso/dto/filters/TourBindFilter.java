package org.novinomad.picasso.dto.filters;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.novinomad.picasso.entities.domain.impl.TourParticipant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourBindFilter extends TourFilter {

    private List<TourParticipant.Type> tourParticipantTypes = new ArrayList<>();

    private List<Long> tourParticipantIds = new ArrayList<>();

    public TourBindFilter() {
        super();
    }

    public TourBindFilter(LocalDateTime startDate, LocalDateTime endDate) {
        super(startDate, endDate);
    }
}
