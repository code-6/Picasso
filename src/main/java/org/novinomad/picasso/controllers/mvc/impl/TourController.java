package org.novinomad.picasso.controllers.mvc.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.util.StringUtil;
import org.h2.engine.Mode;
import org.novinomad.picasso.domain.entities.impl.Tour;
import org.novinomad.picasso.dto.filters.TourFilter;
import org.novinomad.picasso.exceptions.base.PicassoException;
import org.novinomad.picasso.services.ITourService;
import org.novinomad.picasso.services.impl.TourService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Controller
@RequestMapping("/tour")
public class TourController {
    final ITourService tourService;

    static final String TOUR_PAGE = "tour/tourPage";

    @Value("${tour-files-folder}")
    private String tourFolder;

    @ModelAttribute("allTours")
    public List<Tour> getAllTours() {
        return tourService.get();
    }

    @ModelAttribute("userLocale")
    public Locale getCurrentUserLocale() {
        return new Locale("RU");
    }

    @GetMapping
    public String list(@ModelAttribute("tourFilter") TourFilter tourFilter, Boolean fragment, Model model) {

        if(fragment == null) fragment = false;

        tourFilter = Optional.ofNullable(tourFilter).orElse(new TourFilter());

        model.addAttribute("tour", new Tour());
        model.addAttribute("tours", tourService.get(tourFilter));
        model.addAttribute("tourFilter", tourFilter);

        return fragment ? "tour/toursTable :: toursTable" : TOUR_PAGE;
    }

    @PostMapping
    public String save(Tour tour, @RequestParam("tourFiles") MultipartFile[] tourFiles, Model model) {

        try {
            tourService.save(tour, Arrays.asList(tourFiles));
        } catch (PicassoException e) {
            model.addAttribute("exception", e.getMessage());
        }

        return "redirect:/tour";
    }

    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("testAttr", "test attr value");
        return TOUR_PAGE;
    }

}
