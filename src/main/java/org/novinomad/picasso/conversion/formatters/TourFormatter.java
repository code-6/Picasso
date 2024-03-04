package org.novinomad.picasso.conversion.formatters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.repositories.jpa.TourRepository;
import org.slf4j.event.Level;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class TourFormatter implements Formatter<Tour> {

    final ObjectMapper objectMapper;
    final TourRepository tourRepository;

    /**
     * @param tourString might be tour primary key or JSON representation.
     * */
    @Override
    public Tour parse(String tourString, Locale locale) throws ParseException {

        // if tourString is tour primary key get from DB
        if(NumberUtils.isCreatable(tourString)) {
            long tourId = Long.parseLong(tourString);
            return tourRepository.findById(tourId)
                    .orElseThrow(() -> new NoSuchElementException("Tour parse failed because there is no tour with id: " + tourId));
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
            return tour.toString();
        }
    }
}