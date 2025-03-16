package com.example.backend.controllers;


import com.example.shared_module.entities.Coordinates;
import com.example.shared_module.entities.DTO.CoordinatesDTO;
import com.example.shared_module.servicies.CoordinatesService;
import com.example.shared_module.servicies.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coordinates")
public class CoordinatesController extends ItemController<CoordinatesDTO, Coordinates> {

    @Autowired
    public CoordinatesController(CoordinatesService service, UserService userService) {
        super(service, Coordinates.class, userService);
    }

    @GetMapping("/get_depends/{id}")
    public Integer getDepends(@PathVariable Integer id) {
        return ((CoordinatesService) (itemService)).getDepends(id).size();
    }
}
