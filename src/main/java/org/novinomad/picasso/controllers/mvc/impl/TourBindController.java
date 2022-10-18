package org.novinomad.picasso.controllers.mvc.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.dto.filters.TourBindFilter;
import org.novinomad.picasso.entities.domain.impl.TourParticipant;
import org.novinomad.picasso.entities.domain.impl.Tour;
import org.novinomad.picasso.entities.domain.impl.TourBind;
import org.novinomad.picasso.dto.bind.BindDateRange;
import org.novinomad.picasso.dto.bind.TourParticipantBindModel;
import org.novinomad.picasso.dto.bind.TourBindModel;
import org.novinomad.picasso.exceptions.base.BaseException;
import org.novinomad.picasso.services.ITourParticipantService;
import org.novinomad.picasso.services.ITourBindService;
import org.novinomad.picasso.services.ITourService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    final ITourParticipantService tourParticipantService;
    final ITourService tourService;
    final ITourBindService tourBindService;

    @ModelAttribute("allTours")
    public List<Tour> getAllTours() {
        return tourService.get();
    }

    @ModelAttribute("tourParticipant")
    public TourParticipant getTourParticipant() {
        return new TourParticipant();
    }

    @ModelAttribute("tour")
    public Tour getTour() {
        return new Tour();
    }

    @GetMapping
    public ModelAndView index(@ModelAttribute("tourBindFilter") final TourBindFilter tourBindFilter) {

        return new ModelAndView("tourBind/tourBind")
                .addObject("tourBindFilter", tourBindFilter)
                .addObject("tourBind", new TourBindModel())
                .addObject("toursForGantt", tourBindService.getForGanttChart(tourBindFilter));
    }

    @GetMapping("/{tourId}")
    public String getTourBindForm(@PathVariable Long tourId, Model model) {
        TourBindModel tourBind;
        if(tourId != null && tourId > 0) {
            TourBindFilter tourBindFilter = new TourBindFilter();
            tourBindFilter.setTourIds(List.of(tourId));
            List<TourBind> tourBinds = tourBindService.get(tourBindFilter);
            tourBind = TourBindModel.fromEntities(tourBinds);
        } else {
            tourBind = new TourBindModel();
        }
        model.addAttribute("tourBind", tourBind);

        return "tourBind/tourBind :: tourBindForm";
    }

    @GetMapping("/ganttTooltipFragment")
    public String getGanttTooltipFragment(@RequestParam String taskId, Model model) {

        model.addAttribute("ctx", CTX);
        model.addAttribute("isTourTask", "84".equals(taskId.substring(0,2)));
        model.addAttribute("entityId", Long.parseLong(taskId.substring(2)));

        return "fragments/ganttTaskTooltip :: ganttTaskTooltip";
    }

    @PostMapping
    public String bind(final TourBindModel tourBind) {
        try {
            tourBindService.bind(tourBind.toEntities());
        } catch (BaseException e) {
            log.error(e.getMessage(), e);
        }
        return "redirect:/";
    }

    @PostMapping(value = "/bindTourParticipant")
    public String bindTourParticipant(final TourBindModel tourBind, Model model) {
        tourBind.bindTourParticipant(new TourParticipant());
        model.addAttribute("tourBind", tourBind);

        return "tourBind/tourBind :: bindResultTourParticipants";
    }

    @PostMapping("/unbindTourParticipant/{tourParticipantRowId}")
    public String unbindTourParticipant(final TourBindModel tourBind, Model model, @PathVariable Integer tourParticipantRowId) {

        try {
            if(tourParticipantRowId != null) {
                tourBind.getTourParticipants().remove(tourParticipantRowId.intValue());
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        model.addAttribute("tourBind", tourBind);

        return "tourBind/tourBind :: bindResultTourParticipants";
    }

    @PostMapping("/bindTourParticipantDateRange/{tourParticipantRowId}")
    public String bindTourParticipantDateRange(final TourBindModel tourBind, Model model, @PathVariable Integer tourParticipantRowId) {
        try {
            if(tourParticipantRowId != null) {
                TourParticipantBindModel tourParticipantBindModel = tourBind.getTourParticipants().get(tourParticipantRowId);
                Tour tour = tourBind.getTour();
                tourParticipantBindModel.getBindIdsToDateRanges().add(new BindDateRange(tour.getDateRange()));
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        model.addAttribute("tourBind", tourBind);

        return "tourBind/tourBind :: bindResultTourParticipants";
    }

    @PostMapping("/unbindTourParticipantDateRange/{tourParticipantRowId}/{tourParticipantDateRangeRowId}")
    public String unbindTourParticipantDateRange(final TourBindModel tourBind, Model model, @PathVariable Integer tourParticipantRowId,
                                          @PathVariable Integer tourParticipantDateRangeRowId) {
        try {
            if(tourParticipantRowId != null && tourParticipantDateRangeRowId != null) {
                TourParticipantBindModel tourParticipantBindModel = tourBind.getTourParticipants().get(tourParticipantRowId);
                tourParticipantBindModel.getBindIdsToDateRanges().remove(tourParticipantDateRangeRowId.intValue());
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        model.addAttribute("tourBind", tourBind);

        return "tourBind/tourBind :: bindResultTourParticipants";
    }
}
