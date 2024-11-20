package com.example.backend.utils;

import com.example.backend.entities.DTO.CarDTO;
import org.springframework.http.ResponseEntity;

public class Validator {

    public static ResponseEntity<?> validate(CarDTO carDTO){
        if (carDTO.getName() == null ||
                carDTO.getName().isEmpty()){
            return new ResponseEntity<>("Car's name cannot be empty", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Car successfully added!", org.springframework.http.HttpStatus.OK);
    }

}
