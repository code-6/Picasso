package org.novinomad.picasso.controllers.mvc.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.h2.engine.Mode;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.dto.filters.TourFilter;
import org.novinomad.picasso.services.ITourService;
import org.novinomad.picasso.services.impl.TourService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/tour")
public class TourController {

    final ITourService tourService;

    @GetMapping
    public ModelAndView list(Model model) {

        ModelAndView modelAndView = new ModelAndView("tour/tourPage");

        TourFilter filter = (TourFilter) Optional.ofNullable(model.getAttribute("filter"))
                .orElse(new TourFilter());

        List<Tour> tours = tourService.get(filter);

        modelAndView.addObject("filter", filter);
        modelAndView.addObject("tour", new Tour());
        modelAndView.addObject("tours", tours);

        return modelAndView;
    }

}
