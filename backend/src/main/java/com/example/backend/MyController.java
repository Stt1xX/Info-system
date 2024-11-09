package com.example.backend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyController {
    @GetMapping("/greeting")
    public String hello(@RequestParam(name = "name", required = false, defaultValue = "Roma") String name, Model model) {
        User user = new User(1L, "Bob", "ramtim1@mail.ru");
        model.addAttribute("user", user);
        return "greeting";
    }
}
