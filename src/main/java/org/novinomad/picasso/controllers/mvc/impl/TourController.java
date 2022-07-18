package org.novinomad.picasso.controllers.mvc.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.h2.engine.Mode;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.dto.filters.TourFilter;
import org.novinomad.picasso.exceptions.base.PicassoException;
import org.novinomad.picasso.services.ITourService;
import org.novinomad.picasso.services.impl.TourService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/tour")
public class TourController {
    final ITourService tourService;

    static final String TOUR_PAGE = "tour/tourPage";

    @GetMapping
    public ModelAndView list(TourFilter tourFilter) {

        tourFilter = Optional.ofNullable(tourFilter).orElse(new TourFilter());

        List<Tour> tours = tourService.get(tourFilter);

        log.debug("GET /tour requested. Filter: {} return allTours count: {}", tourFilter, tours.size());

        ModelAndView modelAndView = new ModelAndView(TOUR_PAGE);
        modelAndView.addObject("tour", new Tour());
        modelAndView.addObject("tours", tours);
        modelAndView.addObject("tourFilter", tourFilter);
        return modelAndView;
    }

    @PostMapping
    public String save(Tour tour, Model model) {

        try {
            tour = tourService.save(tour);
        } catch (PicassoException e) {
            model.addAttribute("exception", e.getMessage());
        }

        TourFilter tourFilter = (TourFilter) model.getAttribute("filter");

        tourFilter = Optional.ofNullable(tourFilter).orElse(new TourFilter());


        List<Tour> tours = tourService.get(tourFilter);

        log.debug("POST /tour requested. Tour: {} Filter: {}  return allTours count: {}",tour, tourFilter, tours.size());

        model.addAttribute("filter", tourFilter);
        model.addAttribute("tours", tours);
        model.addAttribute("tour", tour);

        return TOUR_PAGE;
    }

    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("testAttr", "test attr value");
        return TOUR_PAGE;
    }

}
