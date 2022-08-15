package org.novinomad.picasso.controllers.restapi.impl;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.commons.LocalDateTimeRange;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.exceptions.base.PicassoException;
import org.novinomad.picasso.services.IEmployeeService;
import org.novinomad.picasso.services.ITourBindService;
import org.novinomad.picasso.services.ITourService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bind")
public class TourBindRestController implements ICrud<TourBind> {

    final IEmployeeService employeeService;
    final ITourService tourService;

    final ITourBindService tourBindService;


    @GetMapping("/validate/{tourId}/{employeeId}/{localDateTimeRange}")
    public void validate(@PathVariable Long tourId,
                                          @PathVariable Long employeeId,
                                          @PathVariable LocalDateTimeRange localDateTimeRange) throws PicassoException {
        tourBindService.validateBind(tourId, employeeId, localDateTimeRange);
    }

    @Override
    @PostMapping
    public TourBind save(TourBind tourBind) throws PicassoException {
        return tourBindService.save(tourBind);
    }

    @Override
    @DeleteMapping
    public void delete(Long id) throws PicassoException {
        tourBindService.delete(id);
    }
}
