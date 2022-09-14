package org.novinomad.picasso.controllers.mvc.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.entities.domain.impl.Tour;
import org.novinomad.picasso.dto.filters.TourFilter;
import org.novinomad.picasso.exceptions.StorageException;
import org.novinomad.picasso.exceptions.base.PicassoException;
import org.novinomad.picasso.services.ITourService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/tour")
public class TourController {
    final ITourService tourService;

    static final String TOUR_PAGE = "tour/tourPage";

    @Value("${app.tour-files-folder}")
    private String tourFolder;

    @ModelAttribute("allTours")
    public List<Tour> getAllTours() {
        return tourService.get();
    }

    @GetMapping("/{tourId}")
    public String getTourFormFragment(@PathVariable Long tourId, Model model) {
        if(tourId != null) {
            tourService.get(tourId)
                    .ifPresentOrElse(tour -> model.addAttribute("tour", tour),
                            () -> model.addAttribute("tour", new Tour()));
        }
        return "tour/tourForm :: tourForm";
    }

    @DeleteMapping
    public String deleteTour(Long tourId) throws PicassoException {
        tourService.delete(tourId);
        return "tour/toursTable :: toursTable";
    }

    @GetMapping
    public String list(@ModelAttribute("tourFilter") TourFilter tourFilter, Boolean fragment, Model model) {

        if(fragment == null) fragment = false;

        tourFilter = Optional.ofNullable(tourFilter).orElse(new TourFilter());

        model.addAttribute("tour", new Tour());
        model.addAttribute("tours", tourService.get(tourFilter));
        model.addAttribute("tourFilter", tourFilter);

        return fragment ? "tour/toursTable :: toursTable" : TOUR_PAGE;
    }

    @PostMapping
    public String save(Tour tour, @RequestParam("tourFiles") MultipartFile[] tourFiles, Model model) {

        try {
            tourService.save(tour, Arrays.asList(tourFiles));
        } catch (PicassoException e) {
            model.addAttribute("exception", e.getMessage());
        }

        return "redirect:/tour";
    }

    @GetMapping("/{tourId}/file/{fileName}")
    public @ResponseBody ResponseEntity<Resource> downloadTourFile(@PathVariable("tourId") Long tourId, @PathVariable("fileName") String fileName) {
        try {
            Resource tourFile = tourService.getTourFile(tourId, fileName);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, tourFile.getFilename());
            httpHeaders.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(tourFile.contentLength()));
            return new ResponseEntity<>(tourFile,
                    httpHeaders,
                    HttpStatus.OK);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(),e );
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage(),e );
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{tourId}/file/{fileName}")
    public String deleteTourFile(@PathVariable("tourId") Long tourId, @PathVariable("fileName") String fileName, Model model) {

        try {
            tourService.deleteTourFile(tourId, fileName);
            tourService.get(tourId).ifPresent(tour -> {
                tour.removeFile(fileName);
                try {
                    tourService.save(tour);
                } catch (PicassoException e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
                model.addAttribute("tour", tour);
            });
        } catch (IOException e) {
            model.addAttribute("exception", e.getMessage());
        }
        return "tour/tourFilesFragment :: tourFilesFragment";
    }
}
