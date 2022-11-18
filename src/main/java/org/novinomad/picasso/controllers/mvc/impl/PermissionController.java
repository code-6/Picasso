package org.novinomad.picasso.controllers.mvc.impl;

import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.erm.dto.VisJsDataSet;
import org.novinomad.picasso.erm.entities.system.Permission;
import org.novinomad.picasso.services.IPermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionController {

    final IPermissionService permissionService;

    @GetMapping
    public ModelAndView showPermissionGraph() {
        VisJsDataSet visJsDataSet = permissionService.asVisJsDataSet();
        return new ModelAndView("permission/permissionPage")
                .addObject("visJsDataSet", visJsDataSet)
                .addObject("permission", new Permission());
    }



}
