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
import org.novinomad.picasso.dto.TourBindDTO;
import org.novinomad.picasso.dto.gantt.Task;
import org.novinomad.picasso.services.impl.TourBindService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/calendar")
public class PicassoCalendarController {

    final TourBindService tourBindService;
    static final LocalDateTime CURRENT_DATE = LocalDateTime.now();
    static final LocalDateTime DEFAULT_START_DATE = CURRENT_DATE.minusMonths(1);
    static final LocalDateTime DEFAULT_END_DATE = CURRENT_DATE.plusMonths(1);


    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    static {
        JSON_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JSON_MAPPER.setDateFormat(new SimpleDateFormat(CommonDateUtils.ISO_8601_WITHOUT_SECONDS));

        JSON_MAPPER.registerModule(new JavaTimeModule());
    }

    @GetMapping
    public ModelAndView ModelAndView() throws JsonProcessingException {
        ModelAndView modelAndView = new ModelAndView("calendar/calendar");

        List<TourBindDTO> forTimeLine = tourBindService.getForTimeLine(DEFAULT_START_DATE, DEFAULT_END_DATE);

        List<Task> toursGanttData = forTimeLine.stream()
                .map(TourBindDTO::dto)
                .toList();

        List<Task> ganttData = new ArrayList<>();

        toursGanttData.forEach(gd -> {
            ganttData.add(gd);
            ganttData.addAll(gd.getChildren());
        });

        modelAndView.addObject("ganttData", JSON_MAPPER.writeValueAsString(ganttData));
        return modelAndView;
    }
}
