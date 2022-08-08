package org.novinomad.picasso.controllers.mvc.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.dto.bind.TourBindDTO;
import org.novinomad.picasso.dto.bind.TourBindFormDTO;
import org.novinomad.picasso.dto.filters.TourBindFilter;
import org.novinomad.picasso.dto.gantt.Task;
import org.novinomad.picasso.exceptions.BindException;
import org.novinomad.picasso.services.IEmployeeService;
import org.novinomad.picasso.services.ITourBindService;
import org.novinomad.picasso.services.ITourService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @ModelAttribute("ganttData")
    public String getGanttData(@ModelAttribute("tourBindFilter") final TourBindFilter tourBindFilter, Model model) {
        try {
            return getGanttJsonData(tourBindFilter);
        } catch (JsonProcessingException e) {
            model.addAttribute("exception", e.getMessage());
            return null;
        }
    }

    @ModelAttribute("allEmployees")
    public List<Employee> getAllEmployees() {
        return employeeService.get();
    }

    @RequestMapping(params = "bindEmployee")
    public String bindEmployee(final TourBindFormDTO tourBindFormDTO) {

        tourBindFormDTO.getEmployeeBinds().add(new TourBindFormDTO.EmployeeBind());

        return "tourBind/tourBind";
    }

    @RequestMapping(params = "unbindEmployee")
    public String unbindEmployee(final TourBindFormDTO tourBindFormDTO,
                                 final HttpServletRequest httpServletRequest) {
        final int rowId = Integer.parseInt(httpServletRequest.getParameter("unbindEmployee"));
        tourBindFormDTO.getEmployeeBinds().remove(rowId);

        return "tourBind/tourBind";
    }

    @GetMapping
    public ModelAndView get(@ModelAttribute("tourBindFilter") final TourBindFilter tourBindFilter,
                            @ModelAttribute("tourBindFormDTO") final TourBindFormDTO tourBindFormDTO,
                            @ModelAttribute("ganttData") final String ganttJsonData) {

        ModelAndView modelAndView = new ModelAndView("tourBind/tourBind");
        //            String ganttJsonData = getGanttJsonData(tourBindFilter);
        modelAndView.addObject("ganttData", ganttJsonData);
        modelAndView.addObject("tourBindFormDTO", tourBindFormDTO);
        modelAndView.addObject("tourBindFilter", tourBindFilter);
        return modelAndView;
    }

    @PostMapping
    public String save(TourBindFormDTO tourBindFormData, Model model, RedirectAttributes attributes) {
        try {
            tourBindService.save(tourBindFormData.forSave());
        } catch (BindException e) {
            model.addAttribute("exception", e.getMessage());
        }
        return "redirect:/";
    }

    private String getGanttJsonData(TourBindFilter tourBindFilter) throws JsonProcessingException {

        if (tourBindFilter == null)
            tourBindFilter = new TourBindFilter();

        List<Task> parentTasks = tourBindService.get(tourBindFilter).stream()
                .collect(Collectors.groupingBy(TourBind::getTour))
                .entrySet().stream()
                .map(e -> new TourBindDTO(e.getKey(), e.getValue()))
                .map(TourBindDTO::dto)
                .toList();

        List<Task> allTasks = new ArrayList<>();

        parentTasks.forEach(gd -> {
            allTasks.add(gd);
            allTasks.addAll(gd.getChildren());
        });

        log.debug("return data for gantt chart. Filter: {} data: {}", tourBindFilter, allTasks);
        return jacksonObjectMapper.writeValueAsString(allTasks);
    }
}
