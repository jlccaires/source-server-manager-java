package com.jlccaires.sourcemanager.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PageController {

    public static final String PAGE = "page";

    @PreAuthorize("isAuthenticated()")
    @RequestMapping
    public String index(Model model) {
        model.addAttribute(PAGE, "index");
        return "index";
    }

    @PreAuthorize("isAnonymous()")
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/hardware")
    public String hardware(Model model) {
        model.addAttribute(PAGE, "hardware");
        return "hardware";
    }

}
