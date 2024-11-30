package com.example.backend.controllers;

import com.example.backend.entities.DTO.HumanDTO;
import com.example.backend.entities.Human;
import com.example.backend.servicies.HumanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/humans")
public class HumanController extends ItemController<HumanDTO, Human> {

    @Autowired
    public HumanController(HumanService service) {
        super(service, Human.class);
    }
}
