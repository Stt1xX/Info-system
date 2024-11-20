package com.example.backend.controllers;

import com.example.backend.entities.Car;
import com.example.backend.entities.DTO.CarDTO;
import com.example.backend.servicies.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCar(@RequestBody CarDTO carDTO) {
        return carService.addCar(carDTO);
    }

    @MessageMapping("/get_cars")
    @SendTo("/topic/cars")
    public List<Car> getAllCars(){
        return carService.getAllCars();
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Integer id, @RequestBody CarDTO carDTO) {
        return carService.updateCar(id, carDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Integer id) {
        return carService.deleteCar(id);
    }
}
