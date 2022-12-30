package org.novinomad.picasso.conversion.formatters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.services.tour_participants.TourParticipantServiceImpl;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class TourParticipantFormatter implements Formatter<TourParticipant> {

    final ObjectMapper objectMapper;
    final TourParticipantServiceImpl tourParticipantService;

    @Override
    public TourParticipant parse(String tourParticipantString, Locale locale) throws ParseException {
        if(NumberUtils.isCreatable(tourParticipantString)) {
            long tourParticipantId = Long.parseLong(tourParticipantString);
            return tourParticipantService.getById(tourParticipantId)
                    .orElseThrow(() -> new ParseException("TourParticipant parse failed because there is no tour with id: " + tourParticipantId, 0));
        } else {
            try {
                return objectMapper.readValue(tourParticipantString, TourParticipant.class);
            } catch (JsonProcessingException e) {
                throw new ParseException("Tour parse failed because invalid JSON: " + tourParticipantString + " " + e.getMessage(), 0);
            }
        }
    }

    @Override
    public String print(TourParticipant tourParticipant, Locale locale) {
        try {
            return objectMapper.writeValueAsString(tourParticipant);
        } catch (JsonProcessingException e) {
            return tourParticipant.toString();
        }
    }
}
