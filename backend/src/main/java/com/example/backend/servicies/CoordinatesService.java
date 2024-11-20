package com.example.backend.servicies;

import com.example.backend.entities.Coordinates;
import com.example.backend.entities.DTO.CoordinatesDTO;
import com.example.backend.repositories.CoordinatesRepository;
import com.example.backend.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoordinatesService {

    private final CoordinatesRepository coordinatesRepository;
    private final UserService userService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public CoordinatesService(CoordinatesRepository coordinatesRepository, UserService userService, SimpMessagingTemplate simpMessagingTemplate) {
        this.coordinatesRepository = coordinatesRepository;
        this.userService = userService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }
    public List<Coordinates> getAllCoordinates(){
        return coordinatesRepository.findAll();
    }

    public ResponseEntity<?> addCoordinate(CoordinatesDTO coordinatesDTO) {
        ResponseEntity<?> resp = Validator.validate(coordinatesDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        Coordinates coordinates = new Coordinates();
        coordinates.setAuthor(userService.getCurrentUsername());
        coordinates.setX(coordinatesDTO.getX());
        coordinates.setY(coordinatesDTO.getY());
        try{
            coordinatesRepository.save(coordinates);
            simpMessagingTemplate.convertAndSend("/topic/coordinates", getAllCoordinates());
            return new ResponseEntity<>("Coordinates successfully added!", org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect coordinate's input data", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> updateCoordinates(Integer id, CoordinatesDTO coordinatesDTO) {
        ResponseEntity<?> resp = Validator.validate(coordinatesDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        Coordinates coordinates = coordinatesRepository.findById(id).orElse(null);
        if (coordinates == null) {
            return new ResponseEntity<>("Error: Coordinates not found", HttpStatus.NOT_FOUND);
        }
        coordinates.setAuthor(userService.getCurrentUsername());
        coordinates.setX(coordinatesDTO.getX());
        coordinates.setY(coordinatesDTO.getY());
        try{
            coordinatesRepository.save(coordinates);
            simpMessagingTemplate.convertAndSend("/topic/coordinates", getAllCoordinates());
            return new ResponseEntity<>("Coordinates successfully updated!", org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect coordinate's input data", HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> deleteCar(Integer id) {
        Coordinates coordinates = coordinatesRepository.findById(id).orElse(null);
        if (coordinates == null) {
            return new ResponseEntity<>("Error: Coordinates not found", HttpStatus.NOT_FOUND);
        }
        coordinatesRepository.delete(coordinates);
        simpMessagingTemplate.convertAndSend("/topic/coordinates", getAllCoordinates());
        return new ResponseEntity<>("Coordinates successfully deleted!", HttpStatus.OK);
    }
}
