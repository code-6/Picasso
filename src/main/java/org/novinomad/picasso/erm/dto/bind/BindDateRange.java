package org.novinomad.picasso.erm.dto.bind;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.commons.LocalDateTimeRange;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BindDateRange {
    private Long bindId;
    private IRange dateRange;

    public BindDateRange(IRange dateRange) {
        this.dateRange = dateRange;
    }
}
