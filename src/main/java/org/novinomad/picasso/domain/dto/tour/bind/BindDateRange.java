package org.novinomad.picasso.domain.dto.tour.bind;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.novinomad.picasso.commons.ILocalDateTimeRange;
import org.novinomad.picasso.commons.LocalDateTimeRange;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BindDateRange {
    private Long bindId;
    private LocalDateTimeRange dateTimeRange;

    public BindDateRange(LocalDateTimeRange dateTimeRange) {
        this.dateTimeRange = dateTimeRange;
    }
}
