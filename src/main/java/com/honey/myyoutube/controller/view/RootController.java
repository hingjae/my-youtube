package com.honey.myyoutube.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class RootController {

    @GetMapping
    public String home() {
        return "redirect:/videos";
    }
}
