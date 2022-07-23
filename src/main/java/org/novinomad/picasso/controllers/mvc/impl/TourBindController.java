package org.novinomad.picasso.controllers.mvc.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.commons.utils.CommonDateUtils;
import org.novinomad.picasso.domain.entities.impl.Employee;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.domain.entities.impl.TourBind;
import org.novinomad.picasso.dto.bind.TourBindDTO;
import org.novinomad.picasso.dto.bind.TourBindFormDTO;
import org.novinomad.picasso.dto.filters.TourBindFilter;
import org.novinomad.picasso.dto.gantt.Task;
import org.novinomad.picasso.exceptions.BindException;
import org.novinomad.picasso.services.impl.EmployeeService;
import org.novinomad.picasso.services.impl.TourBindService;
import org.novinomad.picasso.services.impl.TourService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
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

    final TourBindService tourBindService;

    final TourService tourService;

    final EmployeeService employeeService;

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    static {
        JSON_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JSON_MAPPER.setDateFormat(new SimpleDateFormat(CommonDateUtils.ISO_8601_WITHOUT_SECONDS));

        JSON_MAPPER.registerModule(new JavaTimeModule());
    }

    @ModelAttribute("tourBindFilter")
    public TourBindFilter getTourBindFilter() {
        TourBindFilter tourBindFilter = new TourBindFilter();
        log.debug("return new tourBindFilter: {}", tourBindFilter);
        return tourBindFilter;
    }

    // for employee type select in filter form
    @ModelAttribute("employeeTypes")
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

    @ModelAttribute("tourBindFormDTO")
    public TourBindFormDTO getTourBindFormDTO() {
        TourBindFormDTO tourBindFormDTO = new TourBindFormDTO();
        log.debug("return new tourBindFormDTO: {}", tourBindFormDTO);
        return tourBindFormDTO;
    }

    @RequestMapping(params = {"bindTour"})
    public String bindTour(final TourBindFormDTO tourBindFormDTO, Long tourId, final BindingResult bindingResult) throws BindException {
        Tour tour = tourService.get(tourId).orElseThrow(() -> new BindException("Tour with id: {} not found", tourId));
        tourBindFormDTO.setTour(tour);
        return "tourBind/tourBind";
    }

    @RequestMapping(params = {"bindEmployee"})
    public String bindEmployee(final TourBindFormDTO tourBindFormDTO, Long employeeId, final BindingResult bindingResult) throws BindException {
        Employee employee = employeeService.get(employeeId).orElseThrow(() -> new BindException("Employee with id: {} not found", employeeId));
        tourBindFormDTO.appointEmployee(employee);
        return "tourBind/tourBind";
    }
    @GetMapping
    public String get(TourBindFilter tourBindFilter, Model model) {

        try {
            String ganttJsonData = getGanttJsonData(tourBindFilter);
            model.addAttribute("ganttData", ganttJsonData);
        } catch (JsonProcessingException e) {
            model.addAttribute("exception", e.getMessage());
        }
        return "tourBind/tourBind";
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

        if(tourBindFilter == null)
            tourBindFilter = getTourBindFilter();

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
        return JSON_MAPPER.writeValueAsString(allTasks);
    }
}
