package org.novinomad.picasso.controllers.mvc.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.dto.filters.TourCriteria;
import org.novinomad.picasso.services.IEmployeeService;
import org.novinomad.picasso.services.ITourBindService;
import org.novinomad.picasso.services.ITourService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class TourBindController {

    final IEmployeeService employeeService;
    final ITourService tourService;
    final ITourBindService tourBindService;
    final ObjectMapper jacksonObjectMapper;

    // for employee type select in filter form
    @ModelAttribute("allEmployeeTypes")
    public List<Employee.Type> getEmployeeTypes() {
        List<Employee.Type> types = Arrays.asList(Employee.Type.values());
        log.debug("return employee types: {}", types);
        return types;
    }

    @ModelAttribute("allTours")
    public List<Tour> getAllTours() {
        List<Tour> tours = tourService.get();
        log.debug("return all tours: {}", tours.size());
        return tours;
    }

    @GetMapping
    public ModelAndView get(@ModelAttribute("tourCriteria") final TourCriteria tourCriteria) {

        return new ModelAndView("tourBind/tourBind")
                .addObject("tourCriteria", tourCriteria)
                .addObject("toursForGantt", tourBindService.getForGanttChart(tourCriteria));
    }
}
