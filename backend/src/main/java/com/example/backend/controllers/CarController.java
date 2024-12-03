package com.example.backend.controllers;

import com.example.backend.entities.Car;
import com.example.backend.entities.DTO.CarDTO;
import com.example.backend.servicies.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cars")
public class CarController extends ItemController<CarDTO, Car> {

    @Autowired
    public CarController(CarService service) {
        super(service, Car.class);
    }

    @GetMapping("/get_depends/{id}")
    public ResponseEntity<?> getDepends(@PathVariable Integer id) {
        return ((CarService) (itemService)).getDependsCount(id);
    }
}