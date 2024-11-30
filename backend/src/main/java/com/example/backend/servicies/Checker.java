package com.example.backend.servicies;

import com.example.backend.entities.DTO.CarDTO;
import com.example.backend.entities.DTO.CoordinatesDTO;
import com.example.backend.entities.DTO.HumanDTO;
import com.example.backend.entities.ManagedEntity;
import com.example.backend.entities.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Checker {

    private final UserService userService;

    @Autowired
    public Checker(UserService userService) {
        this.userService = userService;
    }

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
        if (humanDTO.getCarId() == null ||
                humanDTO.getCarId() < 0){
            return new ResponseEntity<>("Error: Human's car ID cannot be empty or lower than 0", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        if (humanDTO.getCoordinatesId() == null ||
                humanDTO.getCoordinatesId() < 0){
            return new ResponseEntity<>("Error: Human's coordinates ID cannot be empty or lower than 0", org.springframework.http.HttpStatus.BAD_REQUEST);
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

    public ResponseEntity<?> check_rights(ManagedEntity entity){
        String username = userService.getCurrentUser().getUsername();
        if (!(userService.getCurrentUser().getRole() == Role.ROLE_ADMIN) && !entity.getAuthor().equals(username)){
            return new ResponseEntity<>("Error: You don't have rights to do this", org.springframework.http.HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>("OK!", org.springframework.http.HttpStatus.OK);
    }
}
