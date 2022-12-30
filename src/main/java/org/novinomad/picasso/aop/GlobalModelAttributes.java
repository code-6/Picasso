package org.novinomad.picasso.aop;

import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Driver;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.services.tour_participants.DriverService;
import org.novinomad.picasso.services.tour_participants.GuideService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    @ModelAttribute("allTourParticipantTypes")
    public List<TourParticipant.Type> getTourParticipantTypes() {
        return Arrays.asList(TourParticipant.Type.values());
    }
}
