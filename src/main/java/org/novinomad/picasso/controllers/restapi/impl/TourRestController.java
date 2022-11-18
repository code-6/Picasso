package org.novinomad.picasso.controllers.restapi.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.erm.entities.Tour;
import org.novinomad.picasso.services.ITourService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tour")
public class TourRestController {

    private final ITourService tourService;

    @PostMapping
    public Tour save(Tour tour) throws CommonRuntimeException {
        return tourService.save(tour);
    }

    @DeleteMapping
    public void delete(@RequestParam(required = false) Long... ids) throws CommonRuntimeException {
        tourService.deleteById(Arrays.asList(ids));
    }
}
