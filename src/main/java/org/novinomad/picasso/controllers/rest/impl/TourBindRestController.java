package org.novinomad.picasso.controllers.rest.impl;

import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.commons.exceptions.BindException;
import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.domain.dto.tour.filters.TourBindFilter;
import org.novinomad.picasso.domain.dto.tour.gantt.Task;
import org.novinomad.picasso.domain.erm.entities.tour.TourBind;
import org.novinomad.picasso.services.tour.TourBindService;
import org.novinomad.picasso.services.tour_participants.TourParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bind")
public class TourBindRestController {

    final TourParticipantService tourParticipantService;

    final TourBindService tourBindService;

    @GetMapping("/validate/{tourId}/{tourParticipantId}/{localDateTimeRange}")
    public void validate(@PathVariable Long tourId,
                                   @PathVariable Long tourParticipantId,
                                   @PathVariable LocalDateTimeRange localDateTimeRange) throws CommonException {
        tourBindService.validateBind(tourId, tourParticipantId, localDateTimeRange);
    }


    @PostMapping
    public TourBind save(TourBind tourBind) throws CommonRuntimeException {
        return tourBindService.save(tourBind);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        tourBindService.deleteById(id);
    }

    @PostMapping(value = "/gantt-tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> getToursForGanttChart(TourBindFilter criteria) {

        return tourBindService.getForGanttChart(Optional.ofNullable(criteria).orElse(new TourBindFilter()));
    }
}
