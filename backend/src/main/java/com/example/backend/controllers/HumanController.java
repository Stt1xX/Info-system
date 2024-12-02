package com.example.backend.controllers;

import com.example.backend.entities.DTO.HumanDTO;
import com.example.backend.entities.Human;
import com.example.backend.entities.enums.Mood;
import com.example.backend.entities.enums.WeaponType;
import com.example.backend.servicies.HumanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/humans")
public class HumanController extends ItemController<HumanDTO, Human> {

    @Autowired
    public HumanController(HumanService service) {
        super(service, Human.class);
    }

    @GetMapping("/get_weapon_types")
    public String[] getWeaponTypes() {
        return Arrays.stream(WeaponType.values()).map(WeaponType::getDisplayName).toArray(String[]::new);
    }

    @GetMapping("/get_moods")
    public String[] getMoods() {
        return Arrays.stream(Mood.values()).map(Mood::getDisplayName).toArray(String[]::new);
    }
}
