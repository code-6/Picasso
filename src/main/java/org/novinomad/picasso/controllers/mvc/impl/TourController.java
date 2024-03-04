package org.novinomad.picasso.controllers.mvc.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.aop.annotations.logging.LogIgnore;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.domain.dto.tour.filters.TourFilter;
import org.novinomad.picasso.services.tour.TourService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/tour")
@Loggable
@SessionAttributes(value = {"tablePageSize", "toursTableFilter"})
public class TourController {
    final TourService tourService;

    static final String TOUR_PAGE = "tour/tourPage";
    static final String FORM_CONTENT_FRAGMENT = TOUR_PAGE + " :: formContentFragment";
    static final String TABLE_FRAGMENT = TOUR_PAGE + " :: tableFragment";
    static final String UPLOADED_FILES_FRAGMENT = TOUR_PAGE + " :: tourUploadedFilesFragment";

    /**
     * Default table page size
     * */
    @Value("${app.default-table-page-size}")
    Integer tablePageSize;

    @LogIgnore
    @ModelAttribute("toursTableFilter")
    public TourFilter toursTableFilter() {
        return new TourFilter();
    }

    /**
     * Defines how many entries to show in table per page. Moved to session attribute to remember chosen count when user navigates between pages.
     * */
    @ModelAttribute("tablePageSize")
    public Integer tablePageSize() {
        return tablePageSize;
    }

    @GetMapping("/{tourId}")
    public ModelAndView getTourFormFragment(@PathVariable Long tourId) {
        ModelAndView modelAndView = new ModelAndView(FORM_CONTENT_FRAGMENT);
        if(tourId != null) {
            tourService.getById(tourId)
                    .ifPresentOrElse(tour -> modelAndView.addObject("tour", tour),
                            () -> modelAndView.addObject("tour", new Tour()));
        }
        return modelAndView;
    }

    @DeleteMapping
    public String deleteTour(Long tourId) {
        tourService.deleteById(tourId);
        return TABLE_FRAGMENT;
    }

    @GetMapping
    public ModelAndView showToursPage(@ModelAttribute("toursTableFilter") TourFilter tourFilter, Boolean fragment) {

        if(fragment == null) fragment = false;

        ModelAndView modelAndView = new ModelAndView( fragment ? TABLE_FRAGMENT : TOUR_PAGE);

        modelAndView.addObject("tour", new Tour());
        try {
            List<Tour> tours = tourService.get(tourFilter);
            modelAndView.addObject("tours", tours);
        } catch (Exception e) {
            modelAndView.addObject("exception", "unable to load tours because " + e.getMessage());
        }
        return modelAndView;
    }

    @PostMapping
    public ModelAndView save(Tour tour,
                             @RequestParam("tourFiles") MultipartFile[] tourFiles,
                             RedirectAttributes redirectAttributes) {
        try {
            Tour save = tourService.save(tour, Arrays.asList(tourFiles));
            redirectAttributes.addFlashAttribute("success", "Tour successfully saved. ID = " + save.getId());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("showTourForm", true);
            redirectAttributes.addFlashAttribute("tour", tour);
            redirectAttributes.addFlashAttribute("exception", "Failed to save tour because " + e.getMessage());
        }
        return new ModelAndView("redirect:/tour");
    }

    @GetMapping("/{tourId}/file/{fileName}")
    public @ResponseBody ResponseEntity<Resource> downloadTourFile(@PathVariable("tourId") Long tourId,
                                                                   @PathVariable("fileName") String fileName) {
        try {
            Resource tourFile = tourService.getTourFile(tourId, fileName);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, tourFile.getFilename());
            httpHeaders.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(tourFile.contentLength()));
            return new ResponseEntity<>(tourFile, httpHeaders, HttpStatus.OK);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(),e );
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/file/{fileName}")
    public ModelAndView deleteTourFile(Tour tour,
                                 @PathVariable("fileName") String fileName) {
        ModelAndView modelAndView = new ModelAndView(UPLOADED_FILES_FRAGMENT);

        tour.deleteFile(fileName);
        modelAndView.addObject(tour);

        return modelAndView;
    }

    @PutMapping("/table/len/{tablePageSize}")
    @ResponseStatus(value = HttpStatus.OK)
    public void changeTablePageSize(@PathVariable Integer tablePageSize, Model model) {
        model.addAttribute("tablePageSize", tablePageSize);
    }
}
