package com.example.consumer.services;

import com.example.consumer.entities.Coordinates;
import com.example.consumer.entities.DTO.CoordinatesDTO;
import com.example.consumer.entities.Human;
import com.example.consumer.entities.enums.EntityType;
import com.example.consumer.repositories.CoordinatesRepository;
import com.example.consumer.repositories.HumanRepository;
import com.example.consumer.services.enums.CounterIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoordinatesService extends ItemService<CoordinatesDTO, Coordinates> {

    private final HumanRepository humanRepository;

    @Autowired
    public CoordinatesService(CoordinatesRepository coordinatesRepository, AuditService auditService, Checker checker, HumanRepository humanRepository) {
        super(coordinatesRepository, auditService, checker, Coordinates.class);
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
        coordinates.setAuthor(UserGlobalService.getCurrentUser().getUsername());
        coordinates.setX(coordinatesDTO.getX());
        coordinates.setY(coordinatesDTO.getY());
        Coordinates savedCoordinates = repository.save(coordinates);
//        simpMessagingTemplate.convertAndSend("/topic/coordinates", getSocketMessage());
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
        String author = UserGlobalService.getCurrentUser().getUsername();
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
//        simpMessagingTemplate.convertAndSend("/topic/coordinates", getSocketMessage());
        ResponseEntity<?> resp = auditService.doCommits(savedCoordinates.stream().map(Coordinates::getId).collect(Collectors.toList()), EntityType.COORDINATES, "Create");
        if (resp.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.badRequest().body(resp.getBody());
        }
        int[] ret_arr = new int[]{0, 0, 0};
        ret_arr[CounterIndex.COORDINATES.getValue()] = savedCoordinates.size();
        return ResponseEntity.ok(ret_arr);
    }
}
