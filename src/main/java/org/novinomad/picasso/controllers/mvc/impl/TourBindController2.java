package org.novinomad.picasso.controllers.mvc.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.domain.dto.tour.bind.TourBindModel;
import org.novinomad.picasso.domain.dto.tour.bind.TourParticipantBindModel;
import org.novinomad.picasso.domain.dto.tour.filters.TourBindFilter;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.services.tour.TourBindService;
import org.novinomad.picasso.services.tour.TourService;
import org.novinomad.picasso.services.tour_participants.TourParticipantService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/bind")
@Loggable
@SessionAttributes(value = {"tableFilter"})
public class TourBindController2 {

    final TourBindService tourBindService;

    final TourService tourService;

    final TourParticipantService tourParticipantService;

    @ModelAttribute("tours")
    public List<Tour> tours() {
        return tourService.get();
    }

    @ModelAttribute("tourBind")
    public TourBindModel tourBind() {
        return new TourBindModel();
    }

    @ModelAttribute("tourParticipants")
    public Map<TourParticipant.Type, List<TourParticipant>> tourParticipants() {
        return tourParticipantService.get().stream().collect(Collectors.groupingBy(TourParticipant::getType));
    }

    @ModelAttribute("tour")
    public Tour tour() {
        return tourBind().getTour();
    }

    @ModelAttribute("tableFilter")
    public TourBindFilter tableFilter() {
        return new TourBindFilter();
    }

    @GetMapping
    public ModelAndView showTouBindsPage(@ModelAttribute("tourBindFilter") final TourBindFilter tourBindFilter) {

        return new ModelAndView("tourBind/tourBindPage")
                .addObject("toursForGantt", tourBindService.getForGanttChart(tourBindFilter));
    }
}
