package com.example.backend.servicies;

import com.example.backend.entities.Car;
import com.example.backend.entities.Coordinates;
import com.example.backend.entities.DTO.CarDTO;
import com.example.backend.entities.DTO.CoordinatesDTO;
import com.example.backend.entities.DTO.HumanDTO;
import com.example.backend.entities.Human;
import com.example.backend.entities.enums.EntityType;
import com.example.backend.repositories.HumanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HumanService extends ItemService<HumanDTO, Human> {

    private final CarService carService;
    private final CoordinatesService coordinatesService;

    @Autowired
    public HumanService(HumanRepository humanRepository, UserService userService, AuditService auditService,
                        SimpMessagingTemplate simpMessagingTemplate, Checker checker,
                        CarService carService, CoordinatesService coordinatesService) {
        super(humanRepository, userService, auditService, simpMessagingTemplate, checker, Human.class);
        this.carService = carService;
        this.coordinatesService = coordinatesService;
    }

    @Override
    public ResponseEntity<?> add(HumanDTO humanDTO) {
        ResponseEntity<?> resp = checker.validate(humanDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            throw new DataIntegrityViolationException((String) resp.getBody());
        }
        getAll().forEach(human -> {
            if (human.getName().equals(humanDTO.getName())) {
                throw new DataIntegrityViolationException("Error: Human with name " + humanDTO.getName() + " already exists");
            }
        });
        Human human = new Human();
        HumanDTO.setHuman(human, humanDTO);
        Car car = carService.findById(humanDTO.getCarId());
        Coordinates coordinates = coordinatesService.findById(humanDTO.getCoordinatesId());
        if (car == null) {
            return new ResponseEntity<>("Error: Car not found", HttpStatus.NOT_FOUND);
        }
        if (coordinates == null) {
            return new ResponseEntity<>("Error: Coordinates not found", HttpStatus.NOT_FOUND);
        }
        human.setCar(car);
        human.setCoordinates(coordinates);
        human.setAuthor(userService.getCurrentUser().getUsername());
        Human updatedHuman = repository.save(human);
        simpMessagingTemplate.convertAndSend("/topic/humans", getSocketMessage());
        resp = auditService.doCommit(updatedHuman.getId(), EntityType.HUMAN, "Create");
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        return new ResponseEntity<>(List.of(String.format("Human %s successfully added!", human.getName()), updatedHuman), org.springframework.http.HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addAll(Map<Integer, HumanDTO> humanDTOs) {
        Set<Human> humans = new HashSet<>(getAll());
        List<Human> newHumans = new ArrayList<>();
        String author = userService.getCurrentUser().getUsername();
        for (Map.Entry<Integer, HumanDTO> humanDTO : humanDTOs.entrySet()) {
            ResponseEntity<?> resp = checker.validate(humanDTO.getValue());
            if (resp.getStatusCode() != HttpStatus.OK) {
                throw new DataIntegrityViolationException("Humans: Line " + humanDTO.getKey() + ": " + resp.getBody());
            }
            Human human = new Human();
            human.setAuthor(author);
            HumanDTO.setHuman(human, humanDTO.getValue());
            if (humanDTO.getValue().getCarId() == null){
                human.setCar(carService.getObjWhileAddFunc(
                        new CarDTO(humanDTO.getValue().getCarName(), humanDTO.getValue().getCarIsCool()
                )));
            } else {
                Car car = carService.findById(humanDTO.getValue().getCarId());
                if (car == null) {
                    throw new DataIntegrityViolationException("Humans: Line " + humanDTO.getKey() + ": " + "Error: Car not found");
                }
                human.setCar(car);
            }
            if (humanDTO.getValue().getCoordinatesId() == null){
                human.setCoordinates(coordinatesService.getObjWhileAddFunc(
                        new CoordinatesDTO(humanDTO.getValue().getX(), humanDTO.getValue().getY()
                )));
            } else {
                Coordinates coordinates = coordinatesService.findById(humanDTO.getValue().getCoordinatesId());
                if (coordinates == null) {
                    throw new DataIntegrityViolationException("Humans: Line " + humanDTO.getKey() + ": " + "Error: Coordinates not found");
                }
                human.setCoordinates(coordinates);
            }
            if (!humans.add(human)) {
                throw new DataIntegrityViolationException("Humans: Line " + humanDTO.getKey() + ": Error: Car with name " + human.getName() + " already exists");
            }
            newHumans.add(human);
        }
        List<Human> savedHumans = repository.saveAll(newHumans);
        simpMessagingTemplate.convertAndSend("/topic/humans", getSocketMessage());
        ResponseEntity<?> resp = auditService.doCommits(savedHumans.stream().map(Human::getId).collect(Collectors.toList()), EntityType.CAR, "Create");
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        return new ResponseEntity<>("Humans successfully added!", org.springframework.http.HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> update(Integer id, HumanDTO humanDTO) {
        ResponseEntity<?> resp = checker.validate(humanDTO);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        Human human = repository.findById(id).orElse(null);
        if (human == null) {
            return new ResponseEntity<>("Error: Human not found", HttpStatus.NOT_FOUND);
        }
        resp = checker.check_rights(human);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        try {
            HumanDTO.setHuman(human, humanDTO);
            Car car = carService.findById(humanDTO.getCarId());
            Coordinates coordinates = coordinatesService.findById(humanDTO.getCoordinatesId());
            if (car == null) {
                return new ResponseEntity<>("Error: Car not found", HttpStatus.NOT_FOUND);
            }
            if (coordinates == null) {
                return new ResponseEntity<>("Error: Coordinates not found", HttpStatus.NOT_FOUND);
            }
            human.setCar(car);
            human.setCoordinates(coordinates);
            Human updatedHuman = repository.save(human);
            simpMessagingTemplate.convertAndSend("/topic/humans", getSocketMessage());
            resp = auditService.doCommit(updatedHuman.getId(), EntityType.HUMAN, "Update");
            if (resp.getStatusCode() != HttpStatus.OK) {
                return resp;
            }
            return new ResponseEntity<>(String.format("Human %s successfully updated!", human.getName()), org.springframework.http.HttpStatus.OK);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Error: Incorrect human's input data", HttpStatus.CONFLICT);
        }
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        Human human = repository.findById(id).orElse(null);
        if (human == null) {
            return new ResponseEntity<>("Error: Human not found", HttpStatus.NOT_FOUND);
        }
        ResponseEntity<?> resp = checker.check_rights(human);
        if (resp.getStatusCode() != HttpStatus.OK) {
            return resp;
        }
        repository.delete(human);
        auditService.deleteCommits(id, EntityType.HUMAN);
        simpMessagingTemplate.convertAndSend("/topic/humans", getSocketMessage());
        return new ResponseEntity<>(String.format("Human %s successfully deleted!", human.getName()), HttpStatus.OK);
    }

    public void save(Human human) {
        repository.save(human);
    }

    public List<Human> findAllByImpactSpeed(Integer impactSpeed) {
        return ((HumanRepository) (this.repository)).findAllByImpactSpeed(impactSpeed);
    }
}
