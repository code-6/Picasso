package org.novinomad.picasso.aop;

import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.entities.domain.impl.Driver;
import org.novinomad.picasso.entities.domain.impl.Guide;
import org.novinomad.picasso.entities.domain.impl.TourParticipant;
import org.novinomad.picasso.services.IDriverService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final IDriverService driverService;

    @ModelAttribute("allTourParticipantTypes")
    public List<TourParticipant.Type> getTourParticipantTypes() {
        return Arrays.asList(TourParticipant.Type.values());
    }

    @ModelAttribute("allLanguages")
    public List<Guide.Language> getLanguages(){
        return Arrays.asList(Guide.Language.values());
    }

    @ModelAttribute("allCars")
    public List<Driver.Car> getAllCars() {
        return driverService.getAllCars();
    }

    @ModelAttribute("allCarBrands")
    public List<String> getAllCarBrands() {
        return driverService.getCarBrands();
    }
}
