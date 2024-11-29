package com.example.backend.controllers;

import com.example.backend.entities.Car;
import com.example.backend.entities.DTO.CarDTO;
import com.example.backend.servicies.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
public class CarController extends ItemController<CarDTO, Car> {

    @Autowired
    public CarController(ItemService<CarDTO, Car> service) {
        super(service, Car.class);
    }
}