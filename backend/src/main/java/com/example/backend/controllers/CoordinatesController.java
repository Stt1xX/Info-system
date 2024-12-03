package com.example.backend.controllers;

import com.example.backend.entities.Coordinates;
import com.example.backend.entities.DTO.CoordinatesDTO;
import com.example.backend.servicies.CoordinatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coordinates")
public class CoordinatesController extends ItemController<CoordinatesDTO, Coordinates> {

    @Autowired
    public CoordinatesController(CoordinatesService service) {
        super(service, Coordinates.class);
    }

    @GetMapping("/get_depends/{id}")
    public ResponseEntity<?> getDepends(@PathVariable Integer id) {
        return ((CoordinatesService) (itemService)).getDependsCount(id);
    }
}
