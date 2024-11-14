package com.example.backend.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MyController {

    private final List<String> strs;


    public MyController() {
        strs = new ArrayList<>();
    }

    @GetMapping({"/login", "/greeting", "/registration"})
    public String login() {
        return "forward:/index.html";
    }

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public List<String> handleMessage(String message) {
        strs.add(message);
        return strs;
    }
}
