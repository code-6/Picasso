package org.novinomad.picasso.aop;

import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.domain.erm.entities.tour_participants.Driver;
import org.novinomad.picasso.domain.erm.entities.tour_participants.TourParticipant;
import org.novinomad.picasso.services.tour_participants.DriverService;
import org.novinomad.picasso.services.tour_participants.GuideService;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
@SessionAttributes(names = {"sidenavCollapsed"})
public class GlobalModelAttributes {

    boolean sidenavCollapsed = false;

    @ModelAttribute("sidenavCollapsed")
    public Boolean sidenavCollapsed() {
        return sidenavCollapsed;
    }

    @ModelAttribute("allTourParticipantTypes")
    public List<TourParticipant.Type> getTourParticipantTypes() {
        return Arrays.asList(TourParticipant.Type.values());
    }

    @PutMapping("/sidenav/collapse/{collapse}")
    @ResponseStatus(value = HttpStatus.OK)
    public void toggleSideBar(@PathVariable Boolean collapse, Model model) {
        sidenavCollapsed = collapse;
        model.addAttribute("sidenavCollapsed", collapse);
    }
}
