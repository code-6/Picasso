package org.novinomad.picasso.services.conversion.formatters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.novinomad.picasso.erm.entities.Tour;
import org.novinomad.picasso.services.impl.TourService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class TourFormatter implements Formatter<Tour> {

    final ObjectMapper objectMapper;
    final TourService tourService;

    /**
     * @param tourString might be tour primary key or JSON representation.
     * */
    @Override
    public Tour parse(String tourString, Locale locale) throws ParseException {

        // if tourString is tour primary key get from DB
        if(NumberUtils.isCreatable(tourString)) {
            long tourId = Long.parseLong(tourString);
            return tourService.get(tourId)
                    .orElseThrow(() -> new ParseException("Tour parse failed because there is no tour with id: " + tourId, 0));
        } else {
            try {
                return objectMapper.readValue(tourString, Tour.class);
            } catch (JsonProcessingException e) {
                throw new ParseException("Tour parse failed because invalid JSON: " + tourString + " " + e.getMessage(), 0);
            }
        }
    }

    /**
     * @return JSON representation of tour, if for some reason parsing to JSON will fail then toStringFull() will be returned.
     * */
    @Override
    public String print(Tour tour, Locale locale) {
        try {
            return objectMapper.writeValueAsString(tour);
        } catch (JsonProcessingException e) {
            return tour.toStringFull();
        }
    }
}
