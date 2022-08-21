package org.novinomad.picasso.controllers.mvc.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.dto.bind.EmployeeBindModel;
import org.novinomad.picasso.dto.bind.TourBindModel;
import org.novinomad.picasso.dto.filters.TourCriteria;
import org.novinomad.picasso.exceptions.BindException;
import org.novinomad.picasso.services.IEmployeeService;
import org.novinomad.picasso.services.ITourBindService;
import org.novinomad.picasso.services.ITourService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
        return Arrays.asList(Employee.Type.values());
    }

    @ModelAttribute("allTours")
    public List<Tour> getAllTours() {
        return tourService.get();
    }

    @GetMapping
    public ModelAndView index(@ModelAttribute("tourCriteria") final TourCriteria tourCriteria) {

        return new ModelAndView("tourBind/tourBind")
                .addObject("tourCriteria", tourCriteria)
                .addObject("tourBind", new TourBindModel())
                .addObject("tour", new Tour())
                .addObject("employee", new Employee())
                .addObject("toursForGantt", tourBindService.getForGanttChart(tourCriteria));
    }

    @PostMapping
    public String saveBind(final TourBindModel tourBind) {
        try {
            tourBindService.save(tourBind.toEntities());
        } catch (BindException e) {
            log.error(e.getMessage(), e);
        }
        return "redirect:/";
    }

    @PostMapping("/bindEmployee")
    public String bindEmployee(final TourBindModel tourBind, Model model) {
        tourBind.bindEmployee(new Employee());
        model.addAttribute("tourBind", tourBind);

        return "tourBind/tourBind :: bindResultEmployees";
    }

    @PostMapping("/unbindEmployee")
    public String unbindEmployee(final TourBindModel tourBind, Model model, HttpServletRequest request) {

        try {
            final int employeeRowId = Integer.parseInt(request.getParameter("employeeRowId"));
            tourBind.getEmployees().remove(employeeRowId);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        model.addAttribute("tourBind", tourBind);

        return "tourBind/tourBind :: bindResultEmployees";
    }

    @PostMapping("/bindEmployeeDateRange")
    public String bindEmployeeDateRange(final TourBindModel tourBind, Model model, HttpServletRequest request) {
        try {
            final int employeeRowId = Integer.parseInt(request.getParameter("employeeRowId"));
            EmployeeBindModel employeeBindModel = tourBind.getEmployees().get(employeeRowId);
            Tour tour = tourBind.getTour();
            employeeBindModel.getDateRanges().add(tour.getDateRange());
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        model.addAttribute("tourBind", tourBind);

        return "tourBind/tourBind :: bindResultEmployees";
    }

    @PostMapping("/unbindEmployeeDateRange")
    public String unbindEmployeeDateRange(final TourBindModel tourBind, Model model, HttpServletRequest request) {
        try {
            final int employeeRowId = Integer.parseInt(request.getParameter("employeeRowId"));
            final int dateRangeRowId = Integer.parseInt(request.getParameter("unbindEmployeeDateRange"));

            EmployeeBindModel employeeBindModel = tourBind.getEmployees().get(employeeRowId);
            employeeBindModel.getDateRanges().remove(dateRangeRowId);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        model.addAttribute("tourBind", tourBind);

        return "tourBind/tourBind :: bindResultEmployees";
    }
}
