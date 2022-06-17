package org.novinomad.picasso.controllers.mvc.impl;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.domain.entities.impl.Driver;
import org.novinomad.picasso.services.impl.DriverService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/driver")
public class DriverController {

    final DriverService driverService;

    @GetMapping
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("driver/driverList");

        List<Driver> drivers = driverService.get();

        modelAndView.addObject("drivers", drivers);

        return modelAndView;
    }
}
