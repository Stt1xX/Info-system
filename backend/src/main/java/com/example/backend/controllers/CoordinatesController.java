package com.example.backend.controllers;

import com.example.backend.entities.Coordinates;
import com.example.backend.entities.DTO.CoordinatesDTO;
import com.example.backend.servicies.CoordinatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coordinates")
public class CoordinatesController extends ItemController<CoordinatesDTO, Coordinates> {

    @Autowired
    public CoordinatesController(CoordinatesService service) {
        super(service, Coordinates.class);
    }

    @MessageMapping("/coords")
    @SendTo("/topic/coordinates")
    public void coords()
    {
        System.out.println("get_reg_requests");
    }
}
