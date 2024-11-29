package com.example.backend.controllers;

import com.example.backend.entities.Coordinates;
import com.example.backend.entities.DTO.CoordinatesDTO;
import com.example.backend.servicies.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coordinates")
public class CoordinatesController extends ItemController<CoordinatesDTO, Coordinates> {

    @Autowired
    public CoordinatesController(ItemService<CoordinatesDTO, Coordinates> service) {
        super(service, Coordinates.class);
    }
}
