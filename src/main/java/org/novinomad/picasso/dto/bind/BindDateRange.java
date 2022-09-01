package org.novinomad.picasso.dto.bind;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.novinomad.picasso.commons.LocalDateTimeRange;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BindDateRange {
    private Long bindId;
    private LocalDateTimeRange dateRange;

    public BindDateRange(LocalDateTimeRange dateRange) {
        this.dateRange = dateRange;
    }
}
