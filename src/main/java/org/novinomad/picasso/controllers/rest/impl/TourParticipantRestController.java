package org.novinomad.picasso.controllers.rest.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Driver;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Guide;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.services.tour_participants.DriverService;
import org.novinomad.picasso.services.tour_participants.TourParticipantService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/tour-participant")
@Loggable
public class TourParticipantRestController {

    final TourParticipantService tourParticipantService;

    final DriverService driverService;


    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public TourParticipant save(TourParticipant tourParticipant) throws CommonException {
        return tourParticipantService.save(tourParticipant);
    }

    @PostMapping(value = "/GUIDE", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public TourParticipant save(Guide tourParticipant) throws CommonException {
        return tourParticipantService.save(tourParticipant);
    }

    @PostMapping(value = "/DRIVER", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public TourParticipant save(Driver tourParticipant) throws CommonException {
        return tourParticipantService.save(tourParticipant);
    }

    @DeleteMapping("/{ids}")
    public void delete(@PathVariable("ids") Collection<Long> ids) throws CommonException {
        for (Long id : ids) {
            tourParticipantService.deleteById(id);
        }
    }

    @GetMapping("/{id}")
    public TourParticipant get(@PathVariable("id") Long id) {
        return tourParticipantService.getById(id).orElseThrow(()->new NoSuchElementException("TourParticipant not found by id: " + id));
    }

    @GetMapping
    public List<TourParticipant> get() {
        return tourParticipantService.get();
    }

    @GetMapping(params = "types")
    public List<TourParticipant> get(@RequestParam("types") TourParticipant.Type ... types) {
        return tourParticipantService.get(Arrays.asList(types));
    }

    @GetMapping("/driver/{carBrand}/model")
    public List<String> getCarModels(@PathVariable String carBrand) {
        return driverService.getCarBrandModels(carBrand);
    }
}
