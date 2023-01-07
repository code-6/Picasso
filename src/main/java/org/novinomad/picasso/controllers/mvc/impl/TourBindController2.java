package org.novinomad.picasso.controllers.mvc.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.domain.dto.tour.bind.TourBindModel;
import org.novinomad.picasso.domain.dto.tour.filters.TourBindFilter;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.services.tour.TourBindService;
import org.novinomad.picasso.services.tour.TourService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/bind")
@Loggable
public class TourBindController2 {

    final TourBindService tourBindService;

    final TourService tourService;

    @ModelAttribute("allTours")
    public List<Tour> allTours() {
        return tourService.get();
    }

    @ModelAttribute("tourBind")
    public TourBindModel tourBind() {
        return new TourBindModel();
    }

    @ModelAttribute("tour")
    public Tour tour() {
        return tourBind().getTour();
    }

    @GetMapping
    public ModelAndView showTouBindsPage(@ModelAttribute("tourBindFilter") final TourBindFilter tourBindFilter) {

        return new ModelAndView("tourBind/tourBindPage")
                .addObject("toursForGantt", tourBindService.getForGanttChart(tourBindFilter));
    }
}
