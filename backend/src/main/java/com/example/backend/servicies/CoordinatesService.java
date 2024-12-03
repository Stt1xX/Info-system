package com.example.backend.servicies;

import com.example.backend.entities.Coordinates;
import com.example.backend.entities.DTO.CoordinatesDTO;
import com.example.backend.repositories.CoordinatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class CoordinatesService extends ItemService<CoordinatesDTO, Coordinates> {

    private final HumanService humanService;

    @Autowired
    public CoordinatesService(CoordinatesRepository coordinatesRepository, UserService userService, SimpMessagingTemplate simpMessagingTemplate, Checker checker, HumanService humanService) {
        super(coordinatesRepository, userService, simpMessagingTemplate, checker);
        this.humanService = humanService;
    }

    @Override
    public ResponseEntity<?> add(CoordinatesDTO coordinatesDTO) {
        ResponseEntity<?> resp = checker.validate(coordinatesDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        Coordinates coordinates = new Coordinates();
        coordinates.setAuthor(userService.getCurrentUser().getUsername());
        coordinates.setX(coordinatesDTO.getX());
        coordinates.setY(coordinatesDTO.getY());
        try{
            repository.save(coordinates);
            simpMessagingTemplate.convertAndSend("/topic/coordinates", getSocketMessage());
            return new ResponseEntity<>("Coordinates successfully added!", org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect coordinate's input data", HttpStatus.CONFLICT);
        }
    }

    @Override
    public ResponseEntity<?> update(Integer id, CoordinatesDTO coordinatesDTO) {
        ResponseEntity<?> resp = checker.validate(coordinatesDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        Coordinates coordinates = repository.findById(id).orElse(null);
        if (coordinates == null) {
            return new ResponseEntity<>("Error: Coordinates not found", HttpStatus.NOT_FOUND);
        }
        resp = checker.check_rights(coordinates);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        coordinates.setAuthor(userService.getCurrentUser().getUsername());
        coordinates.setX(coordinatesDTO.getX());
        coordinates.setY(coordinatesDTO.getY());
        try{
            repository.save(coordinates);
            simpMessagingTemplate.convertAndSend("/topic/coordinates", getSocketMessage());
            return new ResponseEntity<>("Coordinates successfully updated!", org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect coordinate's input data", HttpStatus.CONFLICT);
        }
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        Coordinates coordinates = repository.findById(id).orElse(null);
        if (coordinates == null) {
            return new ResponseEntity<>("Error: Coordinates not found", HttpStatus.NOT_FOUND);
        }
        ResponseEntity<?> resp = checker.check_rights(coordinates);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        boolean update = false;
        if (getDependsCount(id).getBody() != null)
            if ((long) getDependsCount(id).getBody() > 0)
                update = true;
        repository.delete(coordinates);
        if (update)
            simpMessagingTemplate.convertAndSend("/topic/humans", getSocketMessage());
        simpMessagingTemplate.convertAndSend("/topic/coordinates", getSocketMessage());
        return new ResponseEntity<>("Coordinates successfully deleted!", HttpStatus.OK);
    }

    public ResponseEntity<?> getDependsCount(Integer id) {
        return humanService.countByCoordinatesId(id);
    }
}
