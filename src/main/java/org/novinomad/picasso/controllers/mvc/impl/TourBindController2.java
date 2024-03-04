package org.novinomad.picasso.controllers.mvc.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.aop.annotations.logging.LogIgnore;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.dto.tour.bind.BindDateRange;
import org.novinomad.picasso.domain.dto.tour.bind.TourBindModel;
import org.novinomad.picasso.domain.dto.tour.bind.TourParticipantBindModel;
import org.novinomad.picasso.domain.dto.tour.filters.TourBindFilter;
import org.novinomad.picasso.domain.dto.tour.gantt.Task;
import org.novinomad.picasso.domain.erm.entities.tour.Tour;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.services.tour.TourBindService;
import org.novinomad.picasso.services.tour.TourService;
import org.novinomad.picasso.services.tour_participants.TourParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/bind")
@Loggable
@SessionAttributes(value = {"ganttChartFilter", "bindFiltersCollapsed", "bindFormCollapsed"})
public class TourBindController2 {

    final TourBindService tourBindService;

    final TourService tourService;

    final TourParticipantService tourParticipantService;

//    private Boolean bindFiltersCollapsed = Boolean.TRUE;

    @LogIgnore
    @ModelAttribute("bindFiltersCollapsed")
    public Boolean bindFiltersCollapsed() {
        return Boolean.TRUE;
    }

    @LogIgnore
    @ModelAttribute("bindFormCollapsed")
    public Boolean bindFormCollapsed() {
        return Boolean.TRUE;
    }

    @PutMapping("/filter-collapse/{bindFiltersCollapsed}")
    @ResponseStatus(value = HttpStatus.OK)
    public void toggleFilterCollapse(@PathVariable Boolean bindFiltersCollapsed, @LogIgnore Model model) {
        model.addAttribute("bindFiltersCollapsed", bindFiltersCollapsed);
    }

    @PutMapping("/form-collapse/{bindFormCollapsed}")
    @ResponseStatus(value = HttpStatus.OK)
    public void toggleFormCollapse(@PathVariable Boolean bindFormCollapsed, @LogIgnore Model model) {
        model.addAttribute("bindFormCollapsed", bindFormCollapsed);
    }

    @LogIgnore
    @ModelAttribute("tours")
    public Map<Long, Tour> tours() {
        return tourService.getMap();
    }

    @LogIgnore
    @ModelAttribute("tourBind")
    public TourBindModel tourBindModel() {
        return new TourBindModel();
    }

    @LogIgnore
    @ModelAttribute("tourParticipantBindModel")
    public TourParticipantBindModel tourParticipantBindModel() {
        return new TourParticipantBindModel();
    }

    @LogIgnore
    @ModelAttribute("tourParticipants")
    public List<TourParticipant> tourParticipants() {
        return tourParticipantService.get();
    }

    @LogIgnore
    @ModelAttribute("tour")
    public Tour tour() {
        return tourBindModel().getTour();
    }

    @LogIgnore
    @ModelAttribute("ganttChartFilter")
    public TourBindFilter ganttChartFilter() {
        return new TourBindFilter();
    }

    @GetMapping
    public ModelAndView showTouBindsPage(@ModelAttribute("ganttChartFilter") final TourBindFilter tourBindFilter, Boolean fragment) {

        if(fragment == null) fragment = false;

        return new ModelAndView(fragment ? "tourBind/tourBindPage :: ganttChartContainerFragment" : "tourBind/tourBindPage")
                .addObject("toursForGantt", tourBindService.getForGanttChart(tourBindFilter));
    }

    @PostMapping(value = "/gantt-tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Task> getToursForGanttChart(@ModelAttribute("ganttChartFilter") TourBindFilter tourBindFilter) {

        return tourBindService.getForGanttChart(Optional.ofNullable(tourBindFilter)
                .orElse(new TourBindFilter()));
    }

    @PostMapping("/add-common-date-range-row")
    public String addTourParticipantDateRange(@ModelAttribute("tourParticipantBindModel") final TourParticipantBindModel tourParticipantBindModel,
                                               @ModelAttribute("tourBind") final TourBindModel tourBind) {

        LocalDateTimeRange dateTimeRange = tourBind.getTour().getDateTimeRange();
        tourParticipantBindModel.getBindIdsToDateRanges().add(new BindDateRange(dateTimeRange));

        return "tourBind/tourBindPage :: inputTourParticipantDateRangeFragment";
    }

    @PostMapping("/delete-common-date-range-row/{dateRangeRowId}")
    public String deleteTourParticipantDateRange(@ModelAttribute("tourParticipantBindModel") final TourParticipantBindModel tourParticipantBindModel,
                                                 @PathVariable Integer dateRangeRowId) {

        if(dateRangeRowId != null && dateRangeRowId > 0) {
            tourParticipantBindModel.getBindIdsToDateRanges().remove(dateRangeRowId.intValue());
        }
        return "tourBind/tourBindPage :: inputTourParticipantDateRangeFragment";
    }

    @PostMapping("/delete-bound-participant/{rowId}")
    public String deleteBoundParticipant(final TourBindModel tourBindModel,
                                         @PathVariable Integer rowId,
                                         Model model) {
        if(rowId != null && rowId >= 0) {
            tourBindModel.getTourParticipants().remove(rowId.intValue());
            model.addAttribute("tourBind", tourBindModel);
        }
        return "tourBind/tourBindPage :: boundTourParticipantsFragment";
    }

    @PostMapping("/edit-bound-participant/{rowId}")
    public String editBoundParticipant(final TourBindModel tourBindModel,
                                       @PathVariable Integer rowId,
                                       Model model) {
        if(rowId != null && rowId >= 0) {
            TourParticipantBindModel tourParticipantBindModel = tourBindModel.getTourParticipants().get(rowId);
            model.addAttribute("tourParticipantBindModel", tourParticipantBindModel);
            model.addAttribute("tourBind", tourBindModel);
        }
        return "tourBind/tourBindPage :: bindParticipantFragment";
    }

    @PostMapping("/bind-participant")
    public String bindParticipant(final TourBindModel tourBindModel,
                                  @ModelAttribute("tourParticipantBindModel") final TourParticipantBindModel tourParticipantBindModel,
                                  Model model) {

        if(tourParticipantBindModel.getTourParticipant() != null) {

            // if already contains model for same participant then replace with new one
            int idx = tourBindModel.getContainingParticipantIndex(tourParticipantBindModel.getTourParticipant());

            if(idx > -1) {
                tourBindModel.getTourParticipants().set(idx, tourParticipantBindModel);
            } else {
                tourBindModel.getTourParticipants().add(tourParticipantBindModel);
            }
            model.addAttribute("tourParticipantBindModel", new TourParticipantBindModel());
            model.addAttribute("tourBind", tourBindModel);
        }
        return "tourBind/tourBindPage :: bindFormParticipantsFragment";
    }
}
