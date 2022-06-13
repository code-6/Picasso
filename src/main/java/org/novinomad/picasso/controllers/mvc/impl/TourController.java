package org.novinomad.picasso.controllers.mvc.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.services.impl.TourService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/tour")
public class TourController {

    final TourService tourService;

    @GetMapping
    public ModelAndView get() {

        ModelAndView modelAndView = new ModelAndView("tours");

        List<Tour> tours = tourService.get();

        modelAndView.addObject("tours", tours);

        return modelAndView;
    }

}
