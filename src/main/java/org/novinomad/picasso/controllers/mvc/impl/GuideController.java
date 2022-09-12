package org.novinomad.picasso.controllers.mvc.impl;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.novinomad.picasso.entities.domain.impl.Guide;
import org.novinomad.picasso.services.impl.GuideService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/guide")
public class GuideController {

    final GuideService guideService;

    @GetMapping
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("guide/guideList");

        List<Guide> guides = guideService.get();
        modelAndView.addObject("guides", guides);

        return modelAndView;
    }
}
