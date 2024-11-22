package com.example.backend.controllers;

import com.example.backend.entities.Car;
import com.example.backend.entities.Coordinates;
import com.example.backend.entities.DTO.CoordinatesDTO;
import com.example.backend.servicies.CoordinatesService;
import com.example.backend.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coordinates")
public class CoordinatesController {

    private final CoordinatesService coordinatesService;

    @Autowired
    public CoordinatesController(CoordinatesService coordinatesService) {
        this.coordinatesService = coordinatesService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCar(@RequestBody CoordinatesDTO coordinatesDTO) {
        return coordinatesService.addCoordinate(coordinatesDTO);
    }

    @MessageMapping("/get_coordinates")
    @SendTo("/topic/coordinates")
    public List<Coordinates> getAllCars(){
        return coordinatesService.getAllCoordinates();
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateCar(@PathVariable Integer id, @RequestBody CoordinatesDTO coordinatesDTO) {
        return coordinatesService.updateCoordinates(id, coordinatesDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Integer id) {
        return coordinatesService.deleteCar(id);
    }

    @GetMapping("/get_sortable_fields")
    public List<String> getSortableFields() {
        return Utils.getSortableFields(Coordinates.class);
    }
}
