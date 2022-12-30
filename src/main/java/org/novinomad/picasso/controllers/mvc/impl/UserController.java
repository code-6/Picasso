package org.novinomad.picasso.controllers.mvc.impl;

import lombok.RequiredArgsConstructor;
import org.novinomad.picasso.aop.annotations.logging.LogIgnore;
import org.novinomad.picasso.aop.annotations.logging.Loggable;
import org.novinomad.picasso.domain.erm.entities.auth.User;
import org.novinomad.picasso.services.auth.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    @ModelAttribute("user")
    public User user() {
        return new User();
    }

    @ModelAttribute("users")
    public List<User> users() {
        return userService.get();
    }

    @GetMapping
    public ModelAndView showUserPage() {
        ModelAndView modelAndView = new ModelAndView("user/userPage");

        return modelAndView;
    }

    @GetMapping("/{username}")
    public String getUserFormFragment(@PathVariable String username, Model model) {
        User user = userService.get(username).orElse(user());

        model.addAttribute(user);

        return "user/userForm :: userFormContent";
    }
}
