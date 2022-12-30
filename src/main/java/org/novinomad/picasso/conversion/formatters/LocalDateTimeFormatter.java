package org.novinomad.picasso.conversion.formatters;

import lombok.RequiredArgsConstructor;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.novinomad.picasso.commons.utils.CommonDateUtils.DEFAULT_LOCALE;
import static org.novinomad.picasso.commons.utils.CommonDateUtils.UI_DATE_TIME_NO_SEC;

@Component
@RequiredArgsConstructor
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {
    @Override
    public LocalDateTime parse(String text, Locale locale) throws ParseException {
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(UI_DATE_TIME_NO_SEC, DEFAULT_LOCALE));
    }

    @Override
    public String print(LocalDateTime object, Locale locale) {
        return object.format(DateTimeFormatter.ofPattern(UI_DATE_TIME_NO_SEC, DEFAULT_LOCALE));
    }
}
