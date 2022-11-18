package org.novinomad.picasso.controllers.mvc.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Collection;
import org.novinomad.picasso.erm.dto.DriverModel;
import org.novinomad.picasso.erm.dto.GuideModel;
import org.novinomad.picasso.erm.entities.Driver;
import org.novinomad.picasso.erm.entities.TourParticipant;
import org.novinomad.picasso.services.ITourParticipantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

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
            model.addAttribute("tourParticipant", tourParticipant.toModel());
            model.addAttribute("driverCar", new Driver.Car());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("participantFragment", tourParticipantType.getThymeleafFragment());
        model.addAttribute("tourParticipantType", tourParticipantType);
        model.addAttribute("tourParticipants", tourParticipantService.get(Collections.singleton(tourParticipantType)));

        return "tourParticipant/tourParticipantPage";
    }

    @GetMapping("/{tourParticipantType}/{tourParticipantId}")
    public String getTourParticipantFormFragment(@PathVariable TourParticipant.Type tourParticipantType,
                                                 @PathVariable Long tourParticipantId, Model model) {

        model.addAttribute("participantFragment", tourParticipantType.getThymeleafFragment());
        model.addAttribute("tourParticipantType", tourParticipantType);
        model.addAttribute("driverCar", new Driver.Car());

        if (tourParticipantId != null) {
            tourParticipantService.get(tourParticipantId)
                    .ifPresentOrElse(tourParticipant -> model.addAttribute("tourParticipant", tourParticipant.toModel()), () -> {
                        try {
                            TourParticipant tourParticipant = tourParticipantType.getTourParticipantClass().getConstructor().newInstance();
                            model.addAttribute("tourParticipant", tourParticipant.toModel());

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
    public String saveGuide(GuideModel guide, Model model) {
        return saveTourParticipant(guide.toEntity(), model);
    }

    @PostMapping("/DRIVER")
    public String saveDriver(DriverModel driver, Model model) {
        return saveTourParticipant(driver.toEntity(), model);
    }

    @PostMapping("/driver/add-car")
    public String addDriverCar(@ModelAttribute("tourParticipant") DriverModel driver,
                               @ModelAttribute("driverCar") Driver.Car car, Model model) {
        driver.addCar(car);

        model.addAttribute("driverCar", new Driver.Car());
        model.addAttribute("tourParticipant", driver);
        model.addAttribute("tourParticipantType", driver.getType());
        model.addAttribute("participantFragment", driver.getType().getThymeleafFragment());

        return "tourParticipant/tourParticipantForm :: tourParticipantForm";
    }

    @PostMapping("/driver/remove-car/{driverCarRowId}")
    public String removeDriverCar(@ModelAttribute("tourParticipant") DriverModel driver,
                                  @PathVariable Integer driverCarRowId, Model model) {

        driver.getCars().remove(driverCarRowId.intValue());

        model.addAttribute("driverCar", new Driver.Car());
        model.addAttribute("tourParticipant", driver);
        model.addAttribute("tourParticipantType", driver.getType());
        model.addAttribute("participantFragment", driver.getType().getThymeleafFragment());

        return "tourParticipant/tourParticipantForm :: tourParticipantForm";
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
