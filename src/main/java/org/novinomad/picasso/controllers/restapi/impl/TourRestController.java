package org.novinomad.picasso.controllers.restapi.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.dto.bind.TourBindDTO;
import org.novinomad.picasso.dto.filters.TourCriteria;
import org.novinomad.picasso.dto.gantt.Task;
import org.novinomad.picasso.exceptions.base.PicassoException;
import org.novinomad.picasso.services.ITourBindService;
import org.novinomad.picasso.services.impl.TourService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tour")
public class TourRestController implements ICrud<Tour> {
    final TourService tourService;

    @Override
    @PostMapping
    public Tour save(Tour tour) throws PicassoException {
        return tourService.save(tour);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws PicassoException {
        tourService.delete(id);
    }

    @GetMapping("/{id}")
    public Tour fetch(@PathVariable("id") Long id) {
        return tourService.get(id).orElseThrow(() -> new NoSuchElementException("Tour not found by id: " + id));
    }

    @Override
    @GetMapping
    public List<Tour> get() {
        return tourService.get();
    }
}
