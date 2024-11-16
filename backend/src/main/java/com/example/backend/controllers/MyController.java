package com.example.backend.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MyController {

    @GetMapping({"/login", "/greeting", "/registration", "/admin/messages"})
    public String mainPage() {
        return "forward:/index.html";
    }

    @GetMapping({"/error", "/error/**"})
    public String errorPage() {
        return "forward:/index.html";
    }
}
