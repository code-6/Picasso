package org.novinomad.picasso.controllers.restapi.impl;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.commons.exceptions.BindException;
import org.novinomad.picasso.commons.exceptions.base.CommonException;
import org.novinomad.picasso.commons.exceptions.base.CommonRuntimeException;
import org.novinomad.picasso.erm.dto.filters.TourBindFilter;
import org.novinomad.picasso.erm.dto.gantt.Task;
import org.novinomad.picasso.erm.entities.TourBind;
import org.novinomad.picasso.services.ITourBindService;
import org.novinomad.picasso.services.ITourParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bind")
public class TourBindRestController {

    final ITourParticipantService tourParticipantService;

    final ITourBindService tourBindService;

    @GetMapping("/validate/{tourId}/{tourParticipantId}/{localDateTimeRange}")
    public ResponseEntity<String> validate(@PathVariable Long tourId,
                                   @PathVariable Long tourParticipantId,
                                   @PathVariable IRange localDateTimeRange) throws CommonException {
        try {
            tourBindService.validateBind(tourId, tourParticipantId, localDateTimeRange);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (BindException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
