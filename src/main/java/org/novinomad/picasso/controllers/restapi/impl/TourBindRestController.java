package org.novinomad.picasso.controllers.restapi.impl;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.commons.ICrud;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.dto.bind.TourBindFormDTO;
import org.novinomad.picasso.exceptions.BindException;
import org.novinomad.picasso.exceptions.base.PicassoException;
import org.novinomad.picasso.services.IEmployeeService;
import org.novinomad.picasso.services.ITourBindService;
import org.novinomad.picasso.services.ITourService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/bind")
public class TourBindRestController implements ICrud<TourBind> {

    final IEmployeeService employeeService;
    final ITourService tourService;

    final ITourBindService tourBindService;

    @PutMapping(value = "/employee")
    public TourBindFormDTO bindEmployee(TourBindFormDTO tourBindFormDTO, @RequestParam Long ... employeeIds) throws BindException {
        List<Employee> employee = employeeService.get(employeeIds);

        tourBindFormDTO.appointEmployee(employee);

        return tourBindFormDTO;
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
