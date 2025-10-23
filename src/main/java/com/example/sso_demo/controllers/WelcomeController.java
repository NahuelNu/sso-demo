package com.example.sso_demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcome(
            @RequestParam(value = "login", required = false) String login,
            Model model) {

        if ("ok".equals(login)) {
            model.addAttribute("message", "¡Login exitoso!");
        }
        return "welcome"; // carga welcome.html
    }
}
