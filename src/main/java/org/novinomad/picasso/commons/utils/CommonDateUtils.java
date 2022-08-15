package org.novinomad.picasso.commons.utils;

import lombok.experimental.UtilityClass;
import org.novinomad.picasso.commons.LocalDateTimeRange;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@UtilityClass
public class CommonDateUtils {

    public static final String ISO_8601 = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_8601_WITHOUT_SECONDS = "yyyy-MM-dd HH:mm";

    public static final String COMMON = "dd MMM yyyy HH:mm";

    public static LocalDateTime dateToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static Date localDateTimeToDate(LocalDateTime dateToConvert) {
        return java.sql.Timestamp.valueOf(dateToConvert);
    }
    
    public static LocalDateTimeRange findOverlappedRange(LocalDateTimeRange range1, LocalDateTimeRange range2) {
        LocalDateTime overlapsStart = null, overlapsEnd = null;
        LocalDateTimeRange overlapsRange = null;

        if (range1.getStartDate().isBefore(range2.getStartDate())) {
            overlapsStart = range2.getStartDate();
        } else if (range1.getStartDate().isAfter(range2.getStartDate())) {
            overlapsStart = range1.getStartDate();
        } else if (range1.getStartDate().isEqual(range2.getStartDate())) {
            overlapsStart = range1.getStartDate();
        }

        if (range1.getEndDate().isBefore(range2.getEndDate())) {
            overlapsEnd = range1.getEndDate();
        } else if (range1.getEndDate().isAfter(range2.getEndDate())) {
            overlapsEnd = range2.getEndDate();
        } else if (range1.getEndDate().isEqual(range2.getEndDate())) {
            overlapsEnd = range1.getEndDate();
        }

        if(overlapsStart != null && overlapsEnd != null)
            overlapsRange = new LocalDateTimeRange(overlapsStart, overlapsEnd);

        return overlapsRange;
    }
}
