package org.novinomad.picasso.controllers.mvc.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.aop.annotations.logging.LogIgnore;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.commons.exceptions.BindException;
import org.novinomad.picasso.domain.dto.tour.filters.TourBindFilter;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.domain.erm.entities.tour.TourBind;
import org.novinomad.picasso.domain.dto.tour.bind.BindDateRange;
import org.novinomad.picasso.domain.dto.tour.bind.TourParticipantBindModel;
import org.novinomad.picasso.domain.dto.tour.bind.TourBindModel;
import org.novinomad.picasso.services.tour_participants.TourParticipantService;
import org.novinomad.picasso.services.tour.TourBindService;
import org.novinomad.picasso.services.tour.TourService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping(TourBindController.CTX)
public class TourBindController {

    public static final String CTX = "/";

    final TourParticipantService tourParticipantService;
    final TourService tourService;
    final TourBindService tourBindService;
    @ModelAttribute("allTours")
    public List<Tour> allTours() {
        return tourService.get();
    }

    @ModelAttribute("tourBindFilter")
    public TourBindFilter tourBindFilter() {
        return new TourBindFilter();
    }

    @ModelAttribute("tourParticipant")
    public TourParticipant tourParticipant() {
        return new TourParticipant();
    }

    @ModelAttribute("tour")
    public Tour tour() {
        return new Tour();
    }

    @GetMapping("/new")
    public ModelAndView showNewTourBindPage() {
        return new ModelAndView("tourBind/tourBindNew");
    }

    @GetMapping
    public ModelAndView showTouBindsPage(@ModelAttribute("tourBindFilter") final TourBindFilter tourBindFilter) {

        return new ModelAndView("tourBind/tourBind")
                .addObject("tourBind", new TourBindModel())
                .addObject("toursForGantt", tourBindService.getForGanttChart(tourBindFilter));
    }

    @GetMapping("/{tourId}")
    public ModelAndView getTourBindForm(@PathVariable Long tourId) {
        TourBindModel tourBind;
        if(tourId != null && tourId > 0) {
            TourBindFilter tourBindFilter = new TourBindFilter();
            tourBindFilter.setTourIds(List.of(tourId));
            List<TourBind> tourBinds = tourBindService.get(tourBindFilter);
            tourBind = TourBindModel.fromEntities(tourBinds);
        } else {
            tourBind = new TourBindModel();
        }
        return new ModelAndView("tourBind/tourBind :: tourBindForm")
                .addObject("tourBind", tourBind);
    }

    @GetMapping("/ganttTooltipFragment")
    public ModelAndView getGanttTooltipFragment(@RequestParam String taskId) {
        return new ModelAndView("fragments/ganttTaskTooltip :: ganttTaskTooltip")
                .addObject("ctx", CTX)
                .addObject("isTourTask", "84".equals(taskId.substring(0, 2)))
                .addObject("entityId", Long.parseLong(taskId.substring(2)));
    }

    @PostMapping
    public String bind(final TourBindModel tourBind) throws BindException {
        tourBindService.bind(tourBind);
        return "redirect:/";
    }

    @PostMapping(value = "/bindTourParticipant")
    public ModelAndView bindTourParticipant(final TourBindModel tourBind) {

        tourBind.bindTourParticipant(new TourParticipant());

        return new ModelAndView("tourBind/tourBind :: bindResultTourParticipants")
                .addObject("tourBind", tourBind);
    }

    @PostMapping("/unbindTourParticipant/{tourParticipantRowId}")
    public ModelAndView unbindTourParticipant(final TourBindModel tourBind, @PathVariable Integer tourParticipantRowId) {

        try {
            if(tourParticipantRowId != null) {
                tourBind.getTourParticipants().remove(tourParticipantRowId.intValue());
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        return new ModelAndView("tourBind/tourBind :: bindResultTourParticipants")
                .addObject("tourBind", tourBind);
    }

    @PostMapping("/bindTourParticipantDateRange/{tourParticipantRowId}")
    public ModelAndView bindTourParticipantDateRange(final TourBindModel tourBind, @PathVariable Integer tourParticipantRowId) {
        try {
            if(tourParticipantRowId != null) {
                TourParticipantBindModel tourParticipantBindModel = tourBind.getTourParticipants().get(tourParticipantRowId);
                Tour tour = tourBind.getTour();
                tourParticipantBindModel.getBindIdsToDateRanges().add(new BindDateRange(tour.getDateTimeRange()));
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        return new ModelAndView("tourBind/tourBind :: bindResultTourParticipants")
                .addObject("tourBind", tourBind);
    }

    @PostMapping("/unbindTourParticipantDateRange/{tourParticipantRowId}/{tourParticipantDateRangeRowId}")
    public ModelAndView unbindTourParticipantDateRange(final TourBindModel tourBind, @PathVariable Integer tourParticipantRowId,
                                          @PathVariable Integer tourParticipantDateRangeRowId) {
        try {
            if(tourParticipantRowId != null && tourParticipantDateRangeRowId != null) {
                TourParticipantBindModel tourParticipantBindModel = tourBind.getTourParticipants().get(tourParticipantRowId);
                tourParticipantBindModel.getBindIdsToDateRanges().remove(tourParticipantDateRangeRowId.intValue());
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        return new ModelAndView("tourBind/tourBind :: bindResultTourParticipants")
                .addObject("tourBind", tourBind);
    }
}
