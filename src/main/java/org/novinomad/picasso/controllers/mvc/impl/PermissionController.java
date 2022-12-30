package org.novinomad.picasso.controllers.mvc.impl;

import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.domain.dto.permissions.VisJsDataSet;
import org.novinomad.picasso.domain.erm.entities.auth.Permission;
import org.novinomad.picasso.services.auth.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionController {

    final PermissionService permissionService;

    @GetMapping
    public ModelAndView showPermissionGraph() {
        VisJsDataSet visJsDataSet = permissionService.getAllForVisJs();
        return new ModelAndView("permission/permissionPage")
                .addObject("visJsDataSet", visJsDataSet)
                .addObject("permission", new Permission());
    }



}
