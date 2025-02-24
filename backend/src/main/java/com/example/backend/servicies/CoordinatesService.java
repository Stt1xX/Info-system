package com.example.backend.servicies;

import com.example.backend.entities.Coordinates;
import com.example.backend.entities.DTO.CoordinatesDTO;
import com.example.backend.entities.Human;
import com.example.backend.entities.enums.EntityType;
import com.example.backend.repositories.CoordinatesRepository;
import com.example.backend.repositories.HumanRepository;
import com.example.backend.servicies.enums.CounterIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoordinatesService extends ItemService<CoordinatesDTO, Coordinates> {

    private final HumanRepository humanRepository;

    @Autowired
    public CoordinatesService(CoordinatesRepository coordinatesRepository, UserService userService, AuditService auditService, SimpMessagingTemplate simpMessagingTemplate, Checker checker, HumanRepository humanRepository) {
        super(coordinatesRepository, userService, auditService, simpMessagingTemplate, checker, Coordinates.class);
        this.humanRepository = humanRepository;
    }

    @Override
    public ResponseEntity<?> add(CoordinatesDTO coordinatesDTO) {
        ResponseEntity<?> resp = checker.validate(coordinatesDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            throw new DataIntegrityViolationException((String) resp.getBody());
        }
        getAll().forEach(coordinates -> {
            if (coordinates.getX().equals(coordinatesDTO.getX()) && coordinates.getY().equals(coordinatesDTO.getY())) {
                throw new DataIntegrityViolationException("Error: Coordinates with X: " + coordinatesDTO.getX() + " and Y: " + coordinatesDTO.getY() + " already exists");
            }
        });
        Coordinates coordinates = new Coordinates();
        coordinates.setAuthor(userService.getCurrentUser().getUsername());
        coordinates.setX(coordinatesDTO.getX());
        coordinates.setY(coordinatesDTO.getY());
        Coordinates savedCoordinates = repository.save(coordinates);
        simpMessagingTemplate.convertAndSend("/topic/coordinates", getSocketMessage());
        resp = auditService.doCommit(savedCoordinates.getId(), EntityType.COORDINATES, "Create");
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        return new ResponseEntity<>(List.of("Coordinates successfully added!", savedCoordinates), org.springframework.http.HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addAll(Map<Integer, CoordinatesDTO> coordinatesDTOs){
        List<Coordinates> newCoordinates = new ArrayList<>();
        Set<Coordinates> mapCoordinates = new HashSet<>(getAll());
        String author = userService.getCurrentUser().getUsername();
        for (Map.Entry<Integer, CoordinatesDTO> coordinatesDTO : coordinatesDTOs.entrySet()) {
            ResponseEntity<?> resp = checker.validate(coordinatesDTO.getValue());
            if (resp.getStatusCode() != HttpStatus.OK) {
                return ResponseEntity.badRequest().body("Coordinates: Line " + coordinatesDTO.getKey() + ": " + resp.getBody());
            }
            Coordinates coordinates = new Coordinates();
            coordinates.setX(coordinatesDTO.getValue().getX());
            coordinates.setY(coordinatesDTO.getValue().getY());
            coordinates.setAuthor(author);
            if (!mapCoordinates.add(coordinates)) {
                return ResponseEntity.badRequest().body("Coordinates: Line " + coordinatesDTO.getKey() + ": Error: Coordinates with X: " + coordinates.getX() + " and Y: " + coordinates.getY() + " already exists");
            }
            newCoordinates.add(coordinates);
        }
        List<Coordinates> savedCoordinates = repository.saveAll(newCoordinates);
        simpMessagingTemplate.convertAndSend("/topic/coordinates", getSocketMessage());
        ResponseEntity<?> resp = auditService.doCommits(savedCoordinates.stream().map(Coordinates::getId).collect(Collectors.toList()), EntityType.COORDINATES, "Create");
        if (resp.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.badRequest().body(resp.getBody());
        }
        int[] ret_arr = new int[]{0, 0, 0};
        ret_arr[CounterIndex.COORDINATES.getValue()] = savedCoordinates.size();
        return ResponseEntity.ok(ret_arr);
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
        Set<Coordinates> coordinatesSet = new HashSet<>(getAll());
        coordinatesSet.remove(coordinates);
        coordinates.setAuthor(userService.getCurrentUser().getUsername());
        coordinates.setX(coordinatesDTO.getX());
        coordinates.setY(coordinatesDTO.getY());
        if (!coordinatesSet.add(coordinates)) {
            return ResponseEntity.badRequest().body("Error: Coordinates with X: " + coordinates.getX() + " and Y: " + coordinates.getY() + " already exists");
        }
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
        return humanRepository.findAllByCoordinates_Id(id);
    }
}
