package com.example.backend.utils;

import com.example.backend.entities.DTO.CarDTO;
import com.example.backend.entities.DTO.CoordinatesDTO;
import org.springframework.http.ResponseEntity;

public class Validator {

    public static ResponseEntity<?> validate(CarDTO carDTO){
        if (carDTO.getName() == null ||
                carDTO.getName().isEmpty()){
            return new ResponseEntity<>("Error: Car's name cannot be empty", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("OK!", org.springframework.http.HttpStatus.OK);
    }

    public static ResponseEntity<?> validate(CoordinatesDTO coordinatesDTO){
        if (coordinatesDTO.getY() < -407){
            return new ResponseEntity<>("Error: Coordinate Y cannot be lower than -407", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("OK!", org.springframework.http.HttpStatus.OK);
    }

}
