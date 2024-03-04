package org.novinomad.picasso.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

public class CommonDurationUtils extends DurationFormatUtils {
    public static String formatDurationWordsNoSec(
            final long durationMillis,
            final boolean suppressLeadingZeroElements,
            final boolean suppressTrailingZeroElements) {

        // This method is generally replaceable by the format method, but
        // there are a series of tweaks and special cases that require
        // trickery to replicate.
        String duration = formatDuration(durationMillis, "d'd 'H'h 'm'm'");
        if (suppressLeadingZeroElements) {
            // this is a temporary marker on the front. Like ^ in regexp.
            duration = " " + duration;
            String tmp = StringUtils.replaceOnce(duration, " 0 d", StringUtils.EMPTY);
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(duration, " 0 h", StringUtils.EMPTY);
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce(duration, " 0 m", StringUtils.EMPTY);
                    duration = tmp;

                }
            }
            if (!duration.isEmpty()) {
                // strip the space off again
                duration = duration.substring(1);
            }
        }
        if (suppressTrailingZeroElements) {
            String tmp = StringUtils.replaceOnce(duration, " 0 m", StringUtils.EMPTY);
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(duration, " 0 h", StringUtils.EMPTY);
                if (tmp.length() != duration.length()) {
                    duration = StringUtils.replaceOnce(tmp, " 0 d", StringUtils.EMPTY);
                }
            }
        }
        // handle plurals
        duration = " " + duration;
//        duration = StringUtils.replaceOnce(duration, " 1 minutes", " 1 minute");
//        duration = StringUtils.replaceOnce(duration, " 1 hours", " 1 hour");
//        duration = StringUtils.replaceOnce(duration, " 1 days", " 1 day");
        return duration.trim();
    }
}
