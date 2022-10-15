package org.novinomad.picasso.controllers.restapi.impl;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.commons.IRange;
import org.novinomad.picasso.dto.filters.TourBindFilter;
import org.novinomad.picasso.entities.domain.impl.TourBind;
import org.novinomad.picasso.dto.gantt.Task;
import org.novinomad.picasso.exceptions.BindException;
import org.novinomad.picasso.exceptions.base.BaseException;
import org.novinomad.picasso.services.ITourParticipantService;
import org.novinomad.picasso.services.ITourBindService;
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
public class TourBindRestController implements ICrud<TourBind> {

    final ITourParticipantService tourParticipantService;

    final ITourBindService tourBindService;

    @GetMapping("/validate/{tourId}/{tourParticipantId}/{localDateTimeRange}")
    public ResponseEntity<String> validate(@PathVariable Long tourId,
                                   @PathVariable Long tourParticipantId,
                                   @PathVariable IRange localDateTimeRange) throws BaseException {
        try {
            tourBindService.validateBind(tourId, tourParticipantId, localDateTimeRange);
            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (BindException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    @PostMapping
    public TourBind save(TourBind tourBind) throws BaseException {
        return tourBindService.save(tourBind);
    }

    @Override
    @DeleteMapping
    public void delete(Long id) throws BaseException {
        tourBindService.delete(id);
    }

    @PostMapping(value = "/gantt-tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> getToursForGanttChart(TourBindFilter criteria) {

        return tourBindService.getForGanttChart(Optional.ofNullable(criteria).orElse(new TourBindFilter()));
    }
}
