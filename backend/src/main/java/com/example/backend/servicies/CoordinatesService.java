package com.example.backend.servicies;

import com.example.backend.entities.Coordinates;
import com.example.backend.entities.DTO.CoordinatesDTO;
import com.example.backend.entities.Human;
import com.example.backend.entities.enums.EntityType;
import com.example.backend.repositories.CoordinatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CoordinatesService extends ItemService<CoordinatesDTO, Coordinates> {

    private final HumanService humanService;

    @Autowired
    public CoordinatesService(CoordinatesRepository coordinatesRepository, UserService userService, AuditService auditService, SimpMessagingTemplate simpMessagingTemplate, Checker checker, HumanService humanService) {
        super(coordinatesRepository, userService, auditService, simpMessagingTemplate, checker, Coordinates.class);
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
            Coordinates savedCoordinates = repository.save(coordinates);
            simpMessagingTemplate.convertAndSend("/topic/coordinates", getSocketMessage());
            resp = auditService.doCommit(savedCoordinates.getId(), EntityType.COORDINATES, "Create");
            if (resp.getStatusCode() != HttpStatus.OK) {
                return resp;
            }
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
            List<Human> depends = getDepends(id);
            Coordinates updatedCoordinates = repository.save(coordinates);
            simpMessagingTemplate.convertAndSend("/topic/coordinates", getSocketMessage());
            if (!depends.isEmpty())
                simpMessagingTemplate.convertAndSend("/topic/humans", getSocketMessage());
            resp = auditService.doCommit(updatedCoordinates.getId(), EntityType.COORDINATES, "Update");
            if (resp.getStatusCode() != HttpStatus.OK) {
                return resp;
            }
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
        List<Human> depends = getDepends(id);
        repository.delete(coordinates);
        if (!depends.isEmpty()){
            for (Human human : depends) {
                auditService.deleteCommits(human.getId(), EntityType.HUMAN);
            }
            simpMessagingTemplate.convertAndSend("/topic/humans", getSocketMessage());
        }
        auditService.deleteCommits(id, EntityType.COORDINATES);
        simpMessagingTemplate.convertAndSend("/topic/coordinates", getSocketMessage());
        return new ResponseEntity<>("Coordinates successfully deleted!", HttpStatus.OK);
    }

    public List<Human> getDepends(Integer id) {
        return humanService.findByCoordinatesId(id);
    }
}
