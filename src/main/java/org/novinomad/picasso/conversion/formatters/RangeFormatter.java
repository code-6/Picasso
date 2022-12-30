package org.novinomad.picasso.conversion.formatters;

import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.commons.ILocalDateTimeRange;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Slf4j
@Component
public class RangeFormatter implements Formatter<ILocalDateTimeRange> {
    /**
     * @param text expected date in format "dd MMM yyyy HH:mm"
     * */
    @Override
    public ILocalDateTimeRange parse(String text, Locale locale) throws ParseException {
        try {
           return LocalDateTimeRange.parse(text, locale);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public String print(ILocalDateTimeRange object, Locale locale) {
        return String.valueOf(object);
    }
}
