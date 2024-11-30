package com.example.backend.controllers;

import com.example.backend.entities.Car;
import com.example.backend.entities.DTO.CarDTO;
import com.example.backend.servicies.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
public class CarController extends ItemController<CarDTO, Car> {

    @Autowired
    public CarController(CarService service) {
        super(service, Car.class);
    }

    @MessageMapping("/cars")
    @SendTo("/topic/cars")
    public void cars()
    {
        System.out.println("get_reg_requests");
    }
}