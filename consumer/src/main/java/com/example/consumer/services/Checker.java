package com.example.consumer.services;

import com.example.consumer.entities.DTO.CarDTO;
import com.example.consumer.entities.DTO.CoordinatesDTO;
import com.example.consumer.entities.DTO.HumanDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Checker {

    public ResponseEntity<?> validate(CarDTO carDTO){
        if (carDTO.getName() == null ||
                carDTO.getName().isEmpty()){
            return new ResponseEntity<>("Error: Car's name cannot be empty", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("OK!", org.springframework.http.HttpStatus.OK);
    }

    public ResponseEntity<?> validate(CoordinatesDTO coordinatesDTO){
        if (coordinatesDTO.getY() < -407){
            return new ResponseEntity<>("Error: Coordinate Y cannot be lower than -407", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("OK!", org.springframework.http.HttpStatus.OK);
    }

    public ResponseEntity<?> validate(HumanDTO humanDTO){
        if (humanDTO.getName() == null ||
                humanDTO.getName().isEmpty()){
            return new ResponseEntity<>("Error: Human's name cannot be empty", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        if (humanDTO.getCarId() == null){
            if (humanDTO.getCarName() == null){
                return new ResponseEntity<>("Error: Human's car ID or name cannot be empty", org.springframework.http.HttpStatus.BAD_REQUEST);
            }
        } else {
            if (humanDTO.getCarId() < 0){
                return new ResponseEntity<>("Error: Human's car ID cannot be lower than 0", org.springframework.http.HttpStatus.BAD_REQUEST);
            }
        }
        if (humanDTO.getCoordinatesId() != null && humanDTO.getCoordinatesId() < 0){
            return new ResponseEntity<>("Error: Human's coordinates ID cannot be lower than 0", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        if (humanDTO.getMood() == null){
            return new ResponseEntity<>("Error: Human's mood cannot be empty", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        if (humanDTO.getWeaponType() == null){
            return new ResponseEntity<>("Error: Human's weapon type cannot be empty", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        if (humanDTO.getSoundtrackName() == null){
            return new ResponseEntity<>("Error: Human's soundtrack name cannot be empty", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        if (humanDTO.getImpactSpeed() == null ||
                humanDTO.getImpactSpeed() > 768){
            return new ResponseEntity<>("Error: Human's impact speed cannot be empty or greater than 768", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("OK!", org.springframework.http.HttpStatus.OK);
    }
}
