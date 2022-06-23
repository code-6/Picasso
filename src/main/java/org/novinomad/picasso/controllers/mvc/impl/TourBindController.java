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
import org.novinomad.picasso.dto.filters.TourBindFilter;
import org.novinomad.picasso.dto.gantt.Task;
import org.novinomad.picasso.services.impl.EmployeeService;
import org.novinomad.picasso.services.impl.TourBindService;
import org.novinomad.picasso.services.impl.TourService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
        return new TourBindFilter();
    }

    // for employee type select in filter form
    @ModelAttribute("allEmployeeTypes")
    public List<Employee.Type> getEmployeeTypes() {
        return Arrays.asList(Employee.Type.values());
    }

    @ModelAttribute("tours")
    public List<Tour> getTours() {
        return tourService.get();
    }

    @GetMapping
    public String get(TourBindFilter tourBindFilter, Model modelMap) {

        try {
            if(tourBindFilter == null)
                tourBindFilter = new TourBindFilter();

            String ganttJsonData = getGanttJsonData(tourBindFilter);
            modelMap.addAttribute("ganttData", ganttJsonData);
        } catch (JsonProcessingException e) {
            modelMap.addAttribute("exception", e.getMessage());
        }
        return "tourBind/tourBind";
    }

    private String getGanttJsonData(TourBindFilter tourBindFilter) throws JsonProcessingException {
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

        return JSON_MAPPER.writeValueAsString(allTasks);
    }
}
