package org.novinomad.picasso.controllers.mvc.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.aop.annotations.logging.LogIgnore;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.domain.dto.tour_participants.DriverDto;
import org.novinomad.picasso.domain.dto.tour_participants.GuideDto;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Driver;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Guide;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.services.tour_participants.DriverService;
import org.novinomad.picasso.services.tour_participants.GuideService;
import org.novinomad.picasso.services.tour_participants.TourParticipantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/tour-participant")
@Loggable
public class TourParticipantController {

    final TourParticipantService tourParticipantService;

    final DriverService driverService;

    final GuideService guideService;

    @LogIgnore
    @ModelAttribute("allLanguages")
    public List<String> getLanguages(){
        return guideService.getLanguages();
    }

    @LogIgnore
    @ModelAttribute("allCars")
    public List<Driver.Car> getAllCars() {
        return driverService.getAllCars();
    }

    @LogIgnore
    @ModelAttribute("allCarBrands")
    public List<String> getAllCarBrands() {
        return driverService.getCarBrands();
    }

    @GetMapping("/{tourParticipantType}")
    public ModelAndView showTourParticipantPage(@PathVariable TourParticipant.Type tourParticipantType) {
        ModelAndView modelAndView = new ModelAndView("tourParticipant/tourParticipantPage");
        try {
            Constructor<? extends TourParticipant> tourParticipantConstructor = tourParticipantType.getTourParticipantClass().getConstructor();
            tourParticipantConstructor.setAccessible(true);
            TourParticipant tourParticipant = tourParticipantConstructor.newInstance();

            modelAndView.addObject("tourParticipant", tourParticipant);
            modelAndView.addObject("tourParticipants", tourParticipantService.get(Collections.singleton(tourParticipantType)));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            modelAndView.addObject("exception", e.getMessage());
        }
        modelAndView.addObject("driverCar", new Driver.Car());
        modelAndView.addObject("participantFragment", tourParticipantType.getThymeleafFragment());
        modelAndView.addObject("tourParticipantType", tourParticipantType);

        return modelAndView;
    }

    @GetMapping("/fragment/table/{tourParticipantType}")
    public ModelAndView getTourParticipantsTableFragment(@PathVariable TourParticipant.Type tourParticipantType) {
        ModelAndView modelAndView = new ModelAndView("tourParticipant/tourParticipantsTable :: tourParticipantsTable");
        modelAndView.addObject("tourParticipantType", tourParticipantType);
        modelAndView.addObject("participantFragment", tourParticipantType.getThymeleafFragment());
        try {
            modelAndView.addObject("tourParticipants", tourParticipantService.get(Collections.singleton(tourParticipantType)));
        } catch (Exception e) {
            modelAndView.addObject("exception", e.getMessage());
        }
        return modelAndView;
    }

    // for edit action
    @GetMapping("/{tourParticipantType}/{tourParticipantId}")
    public ModelAndView getTourParticipantFormFragment(@PathVariable TourParticipant.Type tourParticipantType,
                                                        @PathVariable Long tourParticipantId) {

        ModelAndView modelAndView = new ModelAndView("tourParticipant/tourParticipantForm :: tourParticipantForm");

        modelAndView.addObject("participantFragment", tourParticipantType.getThymeleafFragment());
        modelAndView.addObject("tourParticipantType", tourParticipantType);
        modelAndView.addObject("driverCar", new Driver.Car());

        if (tourParticipantId != null) {
            tourParticipantService.getById(tourParticipantId)
                    .ifPresentOrElse(tourParticipant -> modelAndView.addObject("tourParticipant", tourParticipant.toModel()), () -> {
                        try {
                            TourParticipant tourParticipant = tourParticipantType.getTourParticipantClass().getConstructor().newInstance();
                            modelAndView.addObject("tourParticipant", tourParticipant.toModel());

                        } catch (Exception e) {
                            log.error("unable to instantiate TourParticipant object {} ",e.getMessage(),e);
                            throw new RuntimeException(e);
                        }
                    });
        }
        return modelAndView;
    }

    //todo: find a way to save generic participant instead of create post mapping for each sub-type of TourParticipant.
    // one possible way is to convert application form url encoded data to JSON and call /api generic route.
    // second variant is to convert MultivaluedMap to exact sub-Class of TourParticipant.
    @PostMapping("/GUIDE")
    public ModelAndView saveGuide(Guide guide) {
        return saveTourParticipant(guide);
    }

    @PostMapping("/DRIVER")
    public ModelAndView saveDriver(Driver driver) {
        return saveTourParticipant(driver);
    }

    @PostMapping("/driver/add-car")
    public ModelAndView addDriverCar(@ModelAttribute("tourParticipant") Driver driver,
                                     @ModelAttribute("driverCar") Driver.Car car) {

        ModelAndView modelAndView = new ModelAndView("tourParticipant/tourParticipantForm :: tourParticipantForm");

        driver.addCar(car);

        modelAndView.addObject("driverCar", new Driver.Car());
        modelAndView.addObject("tourParticipant", driver);
        modelAndView.addObject("tourParticipantType", driver.getType());
        modelAndView.addObject("participantFragment", driver.getType().getThymeleafFragment());

        return modelAndView;
    }

    @PostMapping("/driver/remove-car/{driverCarRowId}")
    public ModelAndView removeDriverCar(@ModelAttribute("tourParticipant") Driver driver,
                                        @PathVariable Integer driverCarRowId) {

        ModelAndView modelAndView = new ModelAndView("tourParticipant/tourParticipantForm :: tourParticipantForm");

        driver.getCars().remove(driverCarRowId.intValue());

        modelAndView.addObject("driverCar", new Driver.Car());
        modelAndView.addObject("tourParticipant", driver);
        modelAndView.addObject("tourParticipantType", driver.getType());
        modelAndView.addObject("participantFragment", driver.getType().getThymeleafFragment());

        return modelAndView;
    }

    @LogIgnore
    private <T extends TourParticipant> ModelAndView saveTourParticipant(T tourParticipant) {
        TourParticipant.Type tourParticipantType = tourParticipant.getType();
        ModelAndView modelAndView = new ModelAndView("redirect:/tour-participant/" + tourParticipantType.name());
        try {
            TourParticipant savedTourParticipant = tourParticipantService.save(tourParticipant);
            return showTourParticipantPage(tourParticipantType)
                    .addObject("success", tourParticipantType.name() + " successfully created. ID = " + savedTourParticipant.getId());
        } catch (Exception e) {
            modelAndView.addObject("exception", e.getMessage());
        }
        return modelAndView;
    }
}
