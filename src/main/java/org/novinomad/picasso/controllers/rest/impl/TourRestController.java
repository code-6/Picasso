package org.novinomad.picasso.controllers.rest.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.commons.exceptions.StorageException;
import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.services.tour.TourService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tour")
@Loggable
@Slf4j
public class TourRestController {

    private final TourService tourService;

    @PostMapping(consumes = {
                MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.MULTIPART_FORM_DATA_VALUE
            },
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Tour save(Tour tour, @RequestParam("tourFiles") MultipartFile[] tourFiles) throws CommonException {
        return tourService.save(tour, Arrays.asList(tourFiles));
    }

    @DeleteMapping
    public void delete(@RequestParam(required = false) Long... ids) throws CommonRuntimeException {
        tourService.deleteById(Arrays.asList(ids));
    }

    @DeleteMapping("/{ids}")
    public void delete(@PathVariable("ids") Collection<Long> ids) throws CommonException {
        for (Long id : ids) {
            try {
                tourService.deleteById(id);
            } catch (Exception e) {
                log.warn("unable to delete physically {} because {} items will be soft deleted.", id, e.getMessage());
                tourService.deleteSoft(id);
            }
        }
    }

    @GetMapping("/{id}")
    public Tour get(@PathVariable Long id) {
        return tourService.getById(id).orElseThrow(() -> new NoSuchElementException("tour with id " + id + " does not exists"));
    }
}
