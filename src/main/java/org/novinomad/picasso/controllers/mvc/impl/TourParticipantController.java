package org.novinomad.picasso.controllers.mvc.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.entities.domain.impl.Driver;
import org.novinomad.picasso.entities.domain.impl.Guide;
import org.novinomad.picasso.entities.domain.impl.Tour;
import org.novinomad.picasso.entities.domain.impl.TourParticipant;
import org.novinomad.picasso.exceptions.base.BaseException;
import org.novinomad.picasso.services.ITourParticipantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.InvocationTargetException;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/tour-participant")
public class TourParticipantController {

    final ITourParticipantService tourParticipantService;

    @GetMapping("/{tourParticipantType}")
    public String getTourParticipantPage(@PathVariable TourParticipant.Type tourParticipantType, Model model) {
        try {
            TourParticipant tourParticipant = tourParticipantType.getTourParticipantClass().getConstructor().newInstance();
            model.addAttribute("tourParticipant", tourParticipant);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("participantFragment", tourParticipantType.getThymeleafFragment());
        model.addAttribute("tourParticipantType", tourParticipantType);
        model.addAttribute("tourParticipants", tourParticipantService.get(tourParticipantType));

        return "tourParticipant/tourParticipantPage";
    }

    @GetMapping("/{tourParticipantType}/{tourParticipantId}")
    public String getTourParticipantFormFragment(@PathVariable TourParticipant.Type tourParticipantType,
                                                 @PathVariable Long tourParticipantId, Model model) {

        model.addAttribute("participantFragment", tourParticipantType.getThymeleafFragment());
        model.addAttribute("tourParticipantType", tourParticipantType);

        if (tourParticipantId != null) {
            tourParticipantService.get(tourParticipantId)
                    .ifPresentOrElse(tourParticipant -> model.addAttribute("tourParticipant", tourParticipant), () -> {
                        try {
                            TourParticipant tourParticipant = tourParticipantType.getTourParticipantClass().getConstructor().newInstance();
                            model.addAttribute("tourParticipant", tourParticipant);

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        return "tourParticipant/tourParticipantForm :: tourParticipantForm";
    }

    //todo: find a way to save generic participant instead of create post mapping for each sub-type of TourParticipant.
    // one possible way is to convert application form url encoded data to JSON and call /api generic route.
    // second variant is to convert MultivaluedMap to exact sub-Class of TourParticipant.
    @PostMapping("/GUIDE")
    public String saveGuide(Guide guide, Model model) {
        return saveTourParticipant(guide, model);
    }

    @PostMapping("/DRIVER")
    public String saveDriver(Driver driver, Model model) {
        return saveTourParticipant(driver, model);
    }

    private <T extends TourParticipant> String saveTourParticipant(T tourParticipant, Model model) {
        try {
            tourParticipantService.save(tourParticipant);
        } catch (Exception e) {
            model.addAttribute("exception", e.getMessage());
        }
        return "redirect:/tour-participant/" + tourParticipant.getType().name();
    }
}
