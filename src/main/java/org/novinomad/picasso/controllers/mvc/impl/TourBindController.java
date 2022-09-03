package org.novinomad.picasso.controllers.mvc.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.dto.bind.BindDateRange;
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
@RequestMapping(TourBindController.CTX)
public class TourBindController {

    public static final String CTX = "/";

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

    @ModelAttribute("employee")
    public Employee getEmployee() {
        return new Employee();
    }

    @ModelAttribute("tour")
    public Tour getTour() {
        return new Tour();
    }

    @GetMapping
    public ModelAndView index(@ModelAttribute("tourCriteria") final TourCriteria tourCriteria) {

        return new ModelAndView("tourBind/tourBind")
                .addObject("tourCriteria", tourCriteria)
                .addObject("tourBind", new TourBindModel())
                .addObject("toursForGantt", tourBindService.getForGanttChart(tourCriteria));
    }

    @GetMapping("/{tourId}")
    public String getTourBindForm(@PathVariable Long tourId, Model model) {
        TourBindModel tourBind;
        if(tourId != null && tourId > 0) {
            TourCriteria tourCriteria = new TourCriteria();
            tourCriteria.setTourIds(List.of(tourId));
            List<TourBind> tourBinds = tourBindService.get(tourCriteria);
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
    public String saveBind(final TourBindModel tourBind) {
        try {
            tourBindService.save(tourBind.toEntities());
        } catch (BindException e) {
            log.error(e.getMessage(), e);
        }
        return "redirect:/";
    }

    @PostMapping(value = "/bindEmployee")
    public String bindEmployee(final TourBindModel tourBind, Model model) {
        tourBind.bindEmployee(new Employee());
        model.addAttribute("tourBind", tourBind);

        return "tourBind/tourBind :: bindResultEmployees";
    }

    @PostMapping("/unbindEmployee/{employeeRowId}")
    public String unbindEmployee(final TourBindModel tourBind, Model model, @PathVariable Integer employeeRowId) {

        try {
            if(employeeRowId != null) {
                tourBind.getEmployees().remove(employeeRowId.intValue());
            }
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
            employeeBindModel.getBindIdsToDateRanges().add(new BindDateRange(tour.getDateRange()));
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
            employeeBindModel.getBindIdsToDateRanges().remove(dateRangeRowId);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        model.addAttribute("tourBind", tourBind);

        return "tourBind/tourBind :: bindResultEmployees";
    }
}
