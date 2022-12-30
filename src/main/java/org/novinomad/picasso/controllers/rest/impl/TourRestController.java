package org.novinomad.picasso.controllers.rest.impl;

import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.services.tour.TourService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tour")
public class TourRestController {

    private final TourService tourService;

    @PostMapping
    public Tour save(Tour tour) throws CommonRuntimeException {
        return tourService.save(tour);
    }

    @DeleteMapping
    public void delete(@RequestParam(required = false) Long... ids) throws CommonRuntimeException {
        tourService.deleteById(Arrays.asList(ids));
    }

    @GetMapping("/{id}")
    public Tour get(@PathVariable Long id) {
        return tourService.getById(id).orElseThrow(() -> new NoSuchElementException("tour with id " + id + " does not exists"));
    }
}
