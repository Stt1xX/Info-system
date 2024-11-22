package com.example.backend.controllers;

import com.example.backend.entities.Car;
import com.example.backend.entities.DTO.CarDTO;
import com.example.backend.entities.DTO.PageRequestDTO;
import com.example.backend.servicies.CarService;
import com.example.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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
    public PagedModel<Car> getAllCars(@Payload PageRequestDTO pageRequest){
        return carService.getAllCars(pageRequest.getPage(), pageRequest.getSize(),
                pageRequest.getSortBy(), pageRequest.isOrder());
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Integer id, @RequestBody CarDTO carDTO) {
        return carService.updateCar(id, carDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Integer id) {
        return carService.deleteCar(id);
    }

    @GetMapping("/get_sortable_fields")
    public List<String> getSortableFields() {
        return Utils.getSortableFields(Car.class);
    }
}
