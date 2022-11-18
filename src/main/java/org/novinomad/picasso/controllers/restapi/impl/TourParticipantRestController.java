package org.novinomad.picasso.controllers.restapi.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.erm.entities.TourParticipant;
import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.novinomad.picasso.services.IDriverService;
import org.novinomad.picasso.services.impl.TourParticipantService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tour-participant")
public class TourParticipantRestController {

    final TourParticipantService tourParticipantService;

    final IDriverService driverService;


    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public TourParticipant save(@RequestBody TourParticipant tourParticipant) throws CommonException {
        return tourParticipantService.save(tourParticipant);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws CommonException {
        tourParticipantService.deleteById(id);
    }

    @GetMapping("/{id}")
    public TourParticipant get(@PathVariable("id") Long id) {
        return tourParticipantService.get(id).orElseThrow(()->new NoSuchElementException("TourParticipant not found by id: " + id));
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
