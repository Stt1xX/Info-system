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

    @GetMapping({"/login", "/greeting"})
    public String login() {
        return "forward:/index.html";
    }

    @MessageMapping("/message") // Отправка сообщений клиентом по пути /app/message
    @SendTo("/topic/messages") // Сервер отправляет сообщения клиентам по адресу /topic/messages
    public List<String> handleMessage(String message) {
        // Здесь можно обработать сообщение
        strs.add(message);
        return strs; // Возвращаем сообщение, чтобы отправить его всем подписчикам
    }
}
