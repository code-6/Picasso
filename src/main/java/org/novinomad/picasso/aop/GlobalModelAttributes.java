package org.novinomad.picasso.aop;

import org.novinomad.picasso.entities.domain.impl.Guide;
import org.novinomad.picasso.entities.domain.impl.TourParticipant;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class GlobalModelAttributes {
    @ModelAttribute("allTourParticipantTypes")
    public List<TourParticipant.Type> getTourParticipantTypes() {
        return Arrays.asList(TourParticipant.Type.values());
    }

    @ModelAttribute("allLanguages")
    public List<Guide.Language> getLanguages(){
        return Arrays.asList(Guide.Language.values());
    }
}
